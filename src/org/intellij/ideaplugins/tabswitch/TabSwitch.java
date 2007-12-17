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
import org.jetbrains.annotations.NotNull;

import javax.swing.KeyStroke;
import java.awt.KeyboardFocusManager;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

final class TabSwitch implements ProjectComponent {

    private static final TabSwitchKeyEventDispatcher tabSwitchKeyEventProcessor =
            new TabSwitchKeyEventDispatcher();

    /**
     * initialized in initComponent()
     * @noinspection InstanceVariableMayNotBeInitialized
     */
    TabSwitchSettings tabSwitchSettings;

    /**
     * @noinspection InstanceVariableMayNotBeInitialized
     * initialized in initComponent()
     */
    UISettings uiSettings;

    private Project project;
    private TabSwitchListener listener = null;
    private WeakReference<OpenFilesDialog> openFilesDialogReference =
            new WeakReference(null);

    TabSwitch(Project project) {
        this.project = project;
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "tabswitch.TabSwitch";
    }

    public static TabSwitch getInstance(Project project) {
        return project.getComponent(TabSwitch.class);
    }

    public void initComponent() {
        tabSwitchSettings = TabSwitchSettings.getInstance();
        uiSettings = UISettings.getInstance();
    }

    public void projectClosed() {
        final OpenFilesDialog openFilesDialog = openFilesDialogReference.get();
        if (openFilesDialog != null) {
            if (!openFilesDialog.isDisposed()) {
                openFilesDialog.dispose();
            }
            openFilesDialogReference.clear();
        }
        final ProjectManager projectManager = ProjectManager.getInstance();
        final Project[] openProjects = projectManager.getOpenProjects();
        if (openProjects.length == 0) {
            final KeyboardFocusManager manager =
                    KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.removeKeyEventDispatcher(tabSwitchKeyEventProcessor);
        }
        listener.dispose();
        project = null;
    }

    public void projectOpened() {
        final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.removeKeyEventDispatcher(tabSwitchKeyEventProcessor);
        manager.addKeyEventDispatcher(tabSwitchKeyEventProcessor);
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        listener = new TabSwitchListener(fileEditorManager);
        fileEditorManager.addFileEditorManagerListener(listener);
        uiSettings.addUISettingsListener(listener);
    }

    public void showOpenFiles(KeyStroke keyStroke) {
        OpenFilesDialog openFilesDialog = openFilesDialogReference.get();
        if (openFilesDialog != null && !openFilesDialog.isDisposed()) {
            return;
        }
        final VirtualFile[] files = listener.getFiles();
        if (files.length < 1) {
            return;
        }
        //final JList list = new JList(files);
        //list.setSelectedIndex(0);
        //list.ensureIndexIsVisible(list.getSelectedIndex());
        //list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.addMouseListener(new MouseAdapter() {
        //    public void mousePressed(MouseEvent event) {
        //        dispose();
        //    }
        //});
        //list.setCellRenderer(new TabSwitchListCellRenderer(project));
        //new PopupChooserBuilder(list).setTitle("Open Files").setMovable(true).createPopup().showCenteredInCurrentWindow(project);
        final int scrollPaneSize = tabSwitchSettings.SCROLL_PANE_SIZE;
        if (uiSettings.EDITOR_TAB_LIMIT > 1 && !tabSwitchSettings.SHOW_RECENT_FILES) {
            openFilesDialog = new OpenFilesDialog(project, "Open Files", files, scrollPaneSize);
        } else {
            openFilesDialog = new OpenFilesDialog(project, "Recent Files", files, scrollPaneSize);
        }
        openFilesDialogReference = new WeakReference(openFilesDialog);
        tabSwitchKeyEventProcessor.register(keyStroke, openFilesDialog);
        openFilesDialog.show(project);
    }

    private class TabSwitchListener
            implements FileEditorManagerListener, UISettingsListener {

        private Stack stack = null;
        private FileEditorManager fileEditorManager;
        private final Object lock = new Object();

        TabSwitchListener(FileEditorManager fileEditorManager) {
            this.fileEditorManager = fileEditorManager;
            final int editorTabLimit = uiSettings.EDITOR_TAB_LIMIT;
            final int recentFilesLimit = uiSettings.RECENT_FILES_LIMIT;
            if (editorTabLimit <= 1 || tabSwitchSettings.SHOW_RECENT_FILES) {
                stack = new Stack(recentFilesLimit);
            } else {
                stack = new Stack(editorTabLimit);
            }
        }

        public void fileClosed(FileEditorManager source, VirtualFile file) {
            if (uiSettings.EDITOR_TAB_LIMIT > 1 && !tabSwitchSettings.SHOW_RECENT_FILES) {
                synchronized (lock) {
                    stack.remove(file);
                }
            }
        }

        public void fileOpened(FileEditorManager source, VirtualFile file) {
            synchronized (lock) {
                stack.remove(file);
                stack.push(file);
            }
        }

        public VirtualFile[] getFiles() {
            if (uiSettings.EDITOR_TAB_LIMIT > 1 && !tabSwitchSettings.SHOW_RECENT_FILES) {
                removeClosedFilesFromStack();
            }
            synchronized (lock) {
                final VirtualFile[] array = (VirtualFile[])stack.toArray(
                        new VirtualFile[stack.size()]);
                reverse(array);
                return array;
            }
        }

        /**
         * To work around the IDEA 5.0.2 bug that FileEditorManagerListener.fileClosed is never called.
         */
        private void removeClosedFilesFromStack() {
            synchronized (lock) {
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

        private void reverse(Object[] array) {
            int left = 0;
            int right = array.length - 1;
            while (left < right) {
                final Object temp = array[left];
                array[left] = array[right];
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
            synchronized (lock) {
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

        public void dispose() {
            uiSettings.removeUISettingsListener(this);
            synchronized (lock) {
                fileEditorManager.removeFileEditorManagerListener(this);
                stack.clear();
            }

        }
    }
}