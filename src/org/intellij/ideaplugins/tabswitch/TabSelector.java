package org.intellij.ideaplugins.tabswitch;

import java.awt.Color;
import javax.swing.Icon;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;

final class TabSelector implements TabSelectorInterface {

    private final VirtualFile[] files;
    private final FileEditorManager fileEditorManager;
    private final FileStatusManager fileStatusManager;

    TabSelector(Project project) {
        final TabSwitchProjectComponent tabSwitchProjectComponent =
                (TabSwitchProjectComponent)project.getComponent(TabSwitchProjectComponent.class);
        files = tabSwitchProjectComponent.getFiles();
        fileEditorManager = FileEditorManager.getInstance(project);
        fileStatusManager = FileStatusManager.getInstance(project);
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
        final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (int i = 0; i < fileEntries.length; i++) {
            final VirtualFile virtualFile = files[i];
            final FileType fileTypeWrapper = fileTypeManager.getFileTypeByFile(virtualFile);
            final Icon icon = fileTypeWrapper.getIcon();
	        final FileStatus status = fileStatusManager.getStatus(virtualFile);
	        final Color statusColor = status.getColor();
            final String name = virtualFile.getName();
            fileEntries[i] = new FileEntry(name, icon, statusColor);
        }
        return fileEntries;
    }
}