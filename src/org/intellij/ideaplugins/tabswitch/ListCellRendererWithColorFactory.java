package org.intellij.ideaplugins.tabswitch;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.IconUtil;

/**
 * Simple ListCellRenderer factory. This is the default one to render the popuped list.
 */
public class ListCellRendererWithColorFactory {

  public ListCellRenderer create(final Project project) {
    return new ColoredListCellRenderer() {
      @Override
      protected void customizeCellRenderer(final JList list,
                                           final Object value,
                                           final int index,
                                           final boolean selected,
                                           final boolean hasFocus) {
        if (value instanceof VirtualFile) {
          VirtualFile file = (VirtualFile) value;
          setIcon(IconUtil.getIcon(file, Iconable.ICON_FLAG_READ_STATUS, project));
          TextAttributes attributes = new TextAttributes(FileStatusManager.getInstance(project).getStatus(file).getColor(),
                                                         null,
                                                         null,
                                                         EffectType.LINE_UNDERSCORE,
                                                         Font.PLAIN);
          append(file.getName(), SimpleTextAttributes.fromTextAttributes(attributes));
        }
      }
    };
  }
}
