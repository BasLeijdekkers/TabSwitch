package org.intellij.ideaplugins.tabswitch;

import com.intellij.ide.ui.UISettings;
import com.intellij.ide.ui.UISettingsListener;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.KeyStroke;
import java.awt.KeyboardFocusManager;
import java.util.HashSet;
import java.util.Set;

final class TabSwitchProjectComponent
		implements ProjectComponent, FileEditorManagerListener, UISettingsListener {

	private static final String COMPONENT_NAME = "tabswitch.TabSwitchProjectComponent";

	private static final TabSwitchKeyEventDispatcher tabSwitchKeyEventProcessor =
			new TabSwitchKeyEventDispatcher();

	private final Object lock = new Object();

	private FileEditorManager fileEditorManager;
	private Stack stack = null;

	TabSwitchProjectComponent(Project project) {
		fileEditorManager = FileEditorManager.getInstance(project);
    }

	public void disposeComponent() {}

    public void fileClosed(FileEditorManager source, VirtualFile file) {
        synchronized(lock) {
            stack.remove(file);
        }
    }

    public void fileOpened(FileEditorManager source, VirtualFile file) {
	    synchronized(lock) {
		    stack.remove(file);
		    stack.push(file);
	    }
    }

    public String getComponentName() {
        return COMPONENT_NAME;
    }

	public VirtualFile[] getFiles() {
		if (UISettings.getInstance().EDITOR_TAB_LIMIT > 1) {
			removeClosedFilesFromStack();
		}
		synchronized(lock) {
			final VirtualFile[] array = (VirtualFile[])stack.toArray(new VirtualFile[stack.size()]);
			reverse(array);
			return array;
		}
    }

	/**
	 * To work around the IDEA 5.0.2 bug that FileEditorManagerListener.fileClosed is never called.
	 */
	private void removeClosedFilesFromStack() {
		synchronized(lock) {
			final VirtualFile[] openFiles = fileEditorManager.getOpenFiles();
			final Set<String> openFilesSet = new HashSet();
			for (final VirtualFile openFile : openFiles) {
				openFilesSet.add(openFile.getPath());
			}
			final VirtualFile[] stackFiles = (VirtualFile[])
					stack.toArray(new VirtualFile[stack.size()]);
			for (final VirtualFile stackFile : stackFiles) {
				if (!openFilesSet.contains(stackFile.getPath())) {
					stack.remove(stackFile);
				}
			}
		}
	}

    public void initComponent() {}

	public void projectClosed() {
		final ProjectManager projectManager = ProjectManager.getInstance();
		final Project[] openProjects = projectManager.getOpenProjects();
		if (openProjects.length == 0) {
			final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.removeKeyEventDispatcher(tabSwitchKeyEventProcessor);
		}
		synchronized(lock) {
			fileEditorManager.removeFileEditorManagerListener(this);
			fileEditorManager = null;
			stack = null;
		}
	}

    public void projectOpened() {
	    final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.removeKeyEventDispatcher(tabSwitchKeyEventProcessor);
	    manager.addKeyEventDispatcher(tabSwitchKeyEventProcessor);

	    final UISettings uiSettings = UISettings.getInstance();
	    final int editorTabLimit = uiSettings.EDITOR_TAB_LIMIT;
	    final int recentFilesLimit = uiSettings.RECENT_FILES_LIMIT;
	    synchronized(lock) {
		    fileEditorManager.addFileEditorManagerListener(this);
		    if (editorTabLimit <= 1) {
			    stack = new Stack(recentFilesLimit);
		    } else {
			    stack = new Stack(editorTabLimit);
		    }
		    uiSettings.addUISettingsListener(this);
	    }
	}

	public static void register(KeyStroke keyStroke,
	                            OpenFilesDialog openFilesDialog) {
		tabSwitchKeyEventProcessor.register(keyStroke, openFilesDialog);
	}

    private static void reverse(Object[] array) {
        int left  = 0;
        int right = array.length-1;
        while (left < right) {
            final Object temp = array[left];
            array[left]  = array[right];
	        left++;
	        array[right] = temp;
	        right--;
        }
    }

    public void selectionChanged(FileEditorManagerEvent event) {
	    final VirtualFile selectedFile = event.getNewFile();
	    // selectedFile may be null e.g. after a switch to a tool window.
	    if (selectedFile != null) {
		    synchronized (lock) {
			    stack.remove(selectedFile);
			    stack.push(selectedFile);
		    }
	    }
    }

	public void uiSettingsChanged(UISettings uiSettings) {
		final int editorTabLimit = uiSettings.EDITOR_TAB_LIMIT;
		final int recentFilesLimit = uiSettings.RECENT_FILES_LIMIT;
		synchronized(lock) {
			if (editorTabLimit != stack.capacity()) {
				if (editorTabLimit <= 1) {
					if (recentFilesLimit != stack.capacity()) {
						stack = new Stack(stack, recentFilesLimit);
					}
				} else {
					stack = new Stack(stack, editorTabLimit);
				}
			}
		}
	}
}