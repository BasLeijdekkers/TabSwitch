package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Color;

final class TabSwitchListCellRenderer extends ColoredListCellRenderer implements ListCellRenderer {

    private PsiManager manager;
    private FileStatusManager fileStatusManager;


    TabSwitchListCellRenderer(Project project) {
        manager = PsiManager.getInstance(project);
        fileStatusManager = FileStatusManager.getInstance(project);
    }

    protected void customizeCellRenderer(JList list, Object value, int index, boolean selected,
                                         boolean hasFocus) {
        if (value instanceof VirtualFile) {
            final VirtualFile virtualFile = (VirtualFile)value;
            final String fileName = virtualFile.getName();
            setIcon(getIcon(virtualFile));
            final Color statusColor = getStatusColor(virtualFile);
            final TextAttributes textattributes =
                    new TextAttributes(statusColor, null, null, EffectType.LINE_UNDERSCORE, 0);
            append(fileName, SimpleTextAttributes.fromTextAttributes(textattributes));
        }
    }

    private Color getStatusColor(VirtualFile virtualFile) {
        final FileStatus status = fileStatusManager.getStatus(virtualFile);
        return status.getColor();
    }

    private Icon getIcon(VirtualFile virtualFile) {
        final PsiFile file = manager.findFile(virtualFile);
        final Icon icon;
        if (file == null) {
            icon = virtualFile.getIcon();
        } else {
            icon = file.getIcon(Iconable.ICON_FLAG_READ_STATUS);
        }
        return icon;
    }
}