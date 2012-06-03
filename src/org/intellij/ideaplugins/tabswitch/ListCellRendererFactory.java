package org.intellij.ideaplugins.tabswitch;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.LightColors;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.IconUtil;

/**
 * Creates a {@link ListCellRenderer} for the project.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public interface ListCellRendererFactory<R extends ListCellRenderer> {

  R create(Project project);

  /**
   * Simple ListCellRenderer factory. This is the default one to render the popuped list.
   */
  class ListCellRendererFactoryImpl implements ListCellRendererFactory<ListCellRenderer> {

    @Override
    public ListCellRenderer create(final Project project) {
      return new ColoredListCellRenderer() {
        @Override
        protected void customizeCellRenderer(final JList list,
                                             final Object value,
                                             final int index,
                                             final boolean selected,
                                             final boolean hasFocus) {
          if (value instanceof VirtualFile) {
            final VirtualFile file = (VirtualFile) value;
            setIcon(IconUtil.getIcon(file, Iconable.ICON_FLAG_READ_STATUS, project));
            final Color color = FileStatusManager.getInstance(project).getStatus(file).getColor();
            final TextAttributes attributes = new TextAttributes(color,
                                                                 null,
                                                                 null,
                                                                 EffectType.LINE_UNDERSCORE,
                                                                 Font.PLAIN);
            append(file.getName(), SimpleTextAttributes.fromTextAttributes(attributes));
            if (!selected && FileEditorManager.getInstance(project).isFileOpen(file)) {
              setBackground(LightColors.SLIGHTLY_GREEN);
            }
          }
        }
      };
    }

  }

}
