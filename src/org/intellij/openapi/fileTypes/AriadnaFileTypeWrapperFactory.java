package org.intellij.openapi.fileTypes;

import com.intellij.openapi.fileTypes.FileType;

final class AriadnaFileTypeWrapperFactory implements FileTypeWrapperFactory {

    public FileTypeWrapper createFileTypeWrapper(FileType fileType) {
        return new AriadnaFileTypeWrapper(fileType);
    }

}
