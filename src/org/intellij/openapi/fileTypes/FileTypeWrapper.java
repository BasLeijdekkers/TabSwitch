package org.intellij.openapi.fileTypes;

import javax.swing.Icon;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;

public abstract class FileTypeWrapper extends FileTypeUnsupportedAdapter {

    private final FileTypeIconMap fileTypeIconMap;

    FileTypeWrapper(FileType fileType) {
        fileTypeIconMap = new FileTypeIconMap(directGetIcon(fileType));
    }

    abstract Icon directGetIcon(FileType fileType);

    public final Icon getIcon(VirtualFile virtualFile) {
        return fileTypeIconMap.getIcon(!virtualFile.isWritable());
    }

}
