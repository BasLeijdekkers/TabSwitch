package org.intellij.openapi.fileTypes;

import com.intellij.openapi.fileTypes.FileType;

final class AuroraFileTypeWrapperFactory implements FileTypeWrapperFactory {

    public FileTypeWrapper createFileTypeWrapper(FileType fileType) {
        return new AuroraFileTypeWrapper(fileType);
    }

}
