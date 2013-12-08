package org.intellij.ideaplugins.tabswitch.component;

import java.awt.Color;
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
class ListCellRendererWithColorFactory {

  ListCellRenderer create(final Project project) {
    return new ColoredListCellRenderer<VirtualFile>() {
      @Override
      protected void customizeCellRenderer(JList list,
                                           VirtualFile file,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        setIcon(IconUtil.getIcon(file, Iconable.ICON_FLAG_READ_STATUS, project));
        append(file.getName(), SimpleTextAttributes.fromTextAttributes(new TextAttributes(getForegroundColor(file, project),
                                                                                          null,
                                                                                          null,
                                                                                          EffectType.LINE_UNDERSCORE,
                                                                                          Font.PLAIN)));
      }
    };
  }

  private Color getForegroundColor(VirtualFile file, Project project) {
    return FileStatusManager.getInstance(project).getStatus(file).getColor();
  }
}
