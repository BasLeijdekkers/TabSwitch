package org.intellij.openapi.fileTypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypeListener;
import com.intellij.openapi.vfs.VirtualFile;

public final class FileTypeManagerWrapper extends FileTypeManagerDelegate {

    private static final FileTypeManager FILE_TYPE_MANAGER_WRAPPER = new FileTypeManagerWrapper(FileTypeManager.getInstance());

    private final FileTypeMap fileTypeMap = new FileTypeMap();

    private FileTypeManagerWrapper(FileTypeManager fileTypeManager) {
        super(fileTypeManager);
    }

    public static FileTypeManager getInstance() {
        return FILE_TYPE_MANAGER_WRAPPER;
    }

	public void registerFileType(FileType type, String[] defaultAssociatedExtensions) {
	}

	public FileType getFileTypeByFile(VirtualFile virtualFile) {
        return fileTypeMap.getFileType(super.getFileTypeByFile(virtualFile));
    }

	public String[] getAssociatedExtensions(FileType type) {
		return new String[0];
	}

	public void dispatchPendingEvents(FileTypeListener listener) {
	}

	public FileType getKnownFileTypeOrAssociate(VirtualFile file) {
		return null;
	}

	public void save() {
	}
}
