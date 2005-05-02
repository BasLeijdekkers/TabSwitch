package org.intellij.openapi.fileTypes;

import com.intellij.openapi.fileTypes.FileType;

interface FileTypeWrapperFactory {

    FileTypeWrapper createFileTypeWrapper(FileType fileType);

}
