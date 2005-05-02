package org.intellij.openapi.fileTypes;

import javax.swing.Icon;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;

abstract class FileTypeUnsupportedAdapter implements FileType {

    private String getClassName() {
        return getClass().getName();
    }

    public Icon getIcon(VirtualFile file) {
        throw new UnsupportedOperationException(getClassName());
    }

    public Icon getIcon() {
        throw new UnsupportedOperationException(getClassName());
    }

    public String getName() {
        throw new UnsupportedOperationException(getClassName());
    }

    public String getDescription() {
        throw new UnsupportedOperationException(getClassName());
    }

    public String[] getAssociatedExtensions() {
        throw new UnsupportedOperationException(getClassName());
    }

    public String getDefaultExtension() {
        throw new UnsupportedOperationException(getClassName());
    }

    public boolean isBinary() {
        throw new UnsupportedOperationException(getClassName());
    }

    public boolean isReadOnly() {
        throw new UnsupportedOperationException(getClassName());
    }

    public boolean isMyFileType(VirtualFile file) {
        throw new UnsupportedOperationException(getClassName());
    }

    public String getCharset() {
        throw new UnsupportedOperationException(getClassName());
    }

}
