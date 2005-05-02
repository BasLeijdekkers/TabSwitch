package org.intellij.openapi.fileTypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeListener;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;

abstract class FileTypeManagerUnsupportedAdapter extends FileTypeManager {

    private String getClassName() {
        return getClass().getName();
    }

    public FileType getFileTypeByFile(VirtualFile virtualFile) {
        throw new UnsupportedOperationException(getClassName());
    }

    public FileType getFileTypeByFileName(String fileName) {
        throw new UnsupportedOperationException(getClassName());
    }

    public FileType getFileTypeByExtension(String extension) {
        throw new UnsupportedOperationException(getClassName());
    }

    public FileType[] getRegisteredFileTypes() {
        throw new UnsupportedOperationException(getClassName());
    }

    public boolean isFileIgnored(String name) {
        throw new UnsupportedOperationException(getClassName());
    }

    public void addFileTypeListener(FileTypeListener listener) {
        throw new UnsupportedOperationException(getClassName());
    }

    public void removeFileTypeListener(FileTypeListener listener) {
        throw new UnsupportedOperationException(getClassName());
    }

}
