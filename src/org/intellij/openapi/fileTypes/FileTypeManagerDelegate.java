package org.intellij.openapi.fileTypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;

abstract class FileTypeManagerDelegate extends FileTypeManagerUnsupportedAdapter {

    private final FileTypeManager fileTypeManager;

    FileTypeManagerDelegate(FileTypeManager fileTypeManager) {
        this.fileTypeManager = fileTypeManager;
    }

    public FileType getFileTypeByFile(VirtualFile virtualFile) {
        return fileTypeManager.getFileTypeByFile(virtualFile);
    }

}
