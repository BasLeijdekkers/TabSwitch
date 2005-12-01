package org.intellij.ideaplugins.tabswitch;

import java.awt.Color;
import javax.swing.Icon;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

final class TabSelector implements TabSelectorInterface {

    private final VirtualFile[] files;
    private final FileEditorManager fileEditorManager;
    private final FileStatusManager fileStatusManager;
	private PsiManager manager;

	TabSelector(Project project) {
	    final TabSwitchProjectComponent tabSwitchProjectComponent =
	            (TabSwitchProjectComponent)project.getComponent(TabSwitchProjectComponent.class);
	    files = tabSwitchProjectComponent.getFiles();
	    fileEditorManager = FileEditorManager.getInstance(project);
	    fileStatusManager = FileStatusManager.getInstance(project);
		manager = PsiManager.getInstance(project);
	}

    public void selected(int selectedIndex) {
        if (selectedIndex > 0) {
            final VirtualFile virtualFile = files[selectedIndex];
            if (virtualFile.isValid()) {
                fileEditorManager.openFile(virtualFile, true);
            }
        }
    }

    FileEntry[] getFileEntries() {
        final FileEntry[] fileEntries = new FileEntry[files.length];
        for (int i = 0; i < fileEntries.length; i++) {
            final VirtualFile virtualFile = files[i];
	        final PsiFile file = manager.findFile(virtualFile);
	        final Icon icon;
	        if (file == null) {
		        icon = virtualFile.getIcon();
	        } else {
		        icon = file.getIcon(Iconable.ICON_FLAG_READ_STATUS);
	        }
	        final FileStatus status = fileStatusManager.getStatus(virtualFile);
	        final Color statusColor = status.getColor();
            final String name = virtualFile.getName();
            fileEntries[i] = new FileEntry(name, icon, statusColor);
        }
        return fileEntries;
    }
}