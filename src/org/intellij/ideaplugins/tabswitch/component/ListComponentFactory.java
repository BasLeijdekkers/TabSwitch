package org.intellij.ideaplugins.tabswitch.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.intellij.ideaplugins.tabswitch.TabSwitchProjectComponent;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBList;
import com.intellij.util.IconUtil;

class ListComponentFactory {

  private final Project project;

  ListComponentFactory(Project project) {
    this.project = project;
  }

  JList create(JLabel pathLabel) {
    JList list = new JBList();
    list.setCellRenderer(new ListCellRendererWithColorFactory().create(project));
    list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.getSelectionModel().addListSelectionListener(new ListSelectionListenerWithPathUpdaterFactory().create(list, pathLabel));
    list.addMouseListener(new ListMouseListener(list));
    return list;
  }

  private class ListMouseListener extends MouseAdapter {
    private final JList list;

    public ListMouseListener(JList list) {
      this.list = list;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      int index = list.locationToIndex(e.getPoint());
      if (index != -1) {
        list.setSelectedIndex(index);
        TabSwitchProjectComponent.getHandler(project).closeAndOpenSelectedFile();
      }
    }
  }

  /**
   * Simple ListCellRenderer factory. This is the default one to render the popped up list.
   */
  private static class ListCellRendererWithColorFactory {

    private ListCellRendererWithColorFactory() {
    }

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

  private static class ListSelectionListenerWithPathUpdaterFactory {

    private ListSelectionListenerWithPathUpdaterFactory() {
    }

    ListSelectionListener create(final JList list, final JLabel pathLabel) {
      return new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent event) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              updatePath(list, pathLabel);
            }
          });
        }
      };
    }

    private void updatePath(JList list, JLabel path) {
      path.setText(getPathTextOrEmptyString(path, list.getSelectedValues()));
    }

    private String getPathTextOrEmptyString(JLabel path, Object[] selectedValues) {
      return onlyOneFileIsSelected(selectedValues)
             ? getPathTextForSelectedFile(path, ((VirtualFile) selectedValues[0]).getParent())
             : "";
    }

    private boolean onlyOneFileIsSelected(Object[] selectedValues) {
      return selectedValues != null && selectedValues.length == 1;
    }

    private String getPathTextForSelectedFile(JLabel path, @Nullable VirtualFile parent) {
      if (parent == null) return "";
      String text = parent.getPresentableUrl();
      FontMetrics fontMetrics = path.getFontMetrics(path.getFont());
      while ((fontMetrics.stringWidth(text) > path.getWidth()) && (text.indexOf(File.separatorChar, 4) > 0)) {
        text = "..." + text.substring(text.indexOf(File.separatorChar, 4));
      }
      return text;
    }
  }
}
