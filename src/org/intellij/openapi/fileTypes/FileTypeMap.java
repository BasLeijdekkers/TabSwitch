package org.intellij.openapi.fileTypes;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileTypes.FileType;

final class FileTypeMap extends ResourceMap {

    private static final FileTypeWrapperFactory FILE_TYPE_WRAPPER_FACTORY;

    private FileType fileType;

    static {
        final int buildNumber = Integer.parseInt(ApplicationInfo.getInstance().getBuildNumber());
	    if (buildNumber < 816) {
            FILE_TYPE_WRAPPER_FACTORY = new AriadnaFileTypeWrapperFactory();
        } else if (buildNumber < 3000) {
            FILE_TYPE_WRAPPER_FACTORY = new AuroraFileTypeWrapperFactory();
        } else {
	        FILE_TYPE_WRAPPER_FACTORY = new AriadnaFileTypeWrapperFactory();
        }
    }

    FileType getFileType(FileType fileType) {
        this.fileType = fileType;
        return (FileType) getResource();
    }

    protected Object getKey() {
        return fileType.getName();
    }

    protected Object createResource() {
        return FILE_TYPE_WRAPPER_FACTORY.createFileTypeWrapper(fileType);
    }

}