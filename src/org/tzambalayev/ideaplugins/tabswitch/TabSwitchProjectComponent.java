package org.tzambalayev.ideaplugins.tabswitch;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;

final class TabSwitchProjectComponent implements ProjectComponent, FileEditorManagerListener {

    private Project project;
    private final Stack stack;

	TabSwitchProjectComponent(Project project) {
        this.project = project;
        stack = new Stack();
    }

    public String getComponentName() {
        return "TabSwitchProjectComponent";
    }

    public void initComponent() {
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        fileEditorManager.addFileEditorManagerListener(this);
    }

	public void disposeComponent() {
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        fileEditorManager.removeFileEditorManagerListener(this);
    }

	VirtualFile[] getFiles() {
        final VirtualFile[] array = (VirtualFile[])stack.toArray(new VirtualFile[stack.size()]);
        reverse(array);
        return array;
    }

    private static void reverse(Object[] array) {
        int left  = 0;
        int right = array.length-1;
        while (left < right) {
            final Object temp = array[left];
            array[left++]  = array[right];
            array[right--] = temp;
        }
    }

    public void projectOpened() {
	}

	public void projectClosed() {
		stack.clear();
		project = null;
	}

    public void fileOpened(FileEditorManager source, VirtualFile file) {
	    synchronized(stack) {
		    stack.remove(file);
		    stack.push(file);
	    }
    }

    public void fileClosed(FileEditorManager source, VirtualFile file) {
        synchronized(stack) {
            stack.remove(file);
        }
    }

    public void selectionChanged(FileEditorManagerEvent event) {
	    final VirtualFile selectedFile = event.getNewFile();
	    // selectedFile may be null e.g. after a switch to a tool window.
	    if (selectedFile != null) {
		    synchronized (stack) {
			    stack.remove(selectedFile);
			    stack.push(selectedFile);
		    }
	    }
    }
}
