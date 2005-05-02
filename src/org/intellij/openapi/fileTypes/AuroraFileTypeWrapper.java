package org.intellij.openapi.fileTypes;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeSupportCapabilities;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.Icon;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class AuroraFileTypeWrapper extends FileTypeWrapper {

    private static final Object[] NULL_PARAMETER = new Object[] {null};
    private static final Method GET_ICON_METHOD;

    static {
        Class[] parameterTypes = new Class[] {VirtualFile.class};
        final Method result;
        try {
            result = FileType.class.getMethod("getIcon", parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        GET_ICON_METHOD = result;
    }

    AuroraFileTypeWrapper(FileType fileType) {
        super(fileType);
    }

    Icon directGetIcon(FileType fileType) {
        final Object result;
        try {
            result = GET_ICON_METHOD.invoke(fileType, NULL_PARAMETER);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable cause;
            if (e == null) {
                cause = e;
            } else {
                cause = e.getCause();
            }
            throw new RuntimeException(cause);
        }
        return (Icon) result;
    }

	public String getCharset(VirtualFile virtualFile) {
		return null;
	}

	public SyntaxHighlighter getHighlighter(Project project) {
		return null;
	}

	public FileTypeSupportCapabilities getSupportCapabilities() {
		return null;
	}

	public StructureViewBuilder getStructureViewBuilder(VirtualFile file, Project project) {
		return null;
	}
}
