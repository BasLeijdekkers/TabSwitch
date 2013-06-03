package org.intellij.ideaplugins.tabswitch.component;

import java.awt.FontMetrics;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * <br> User: must <br> Date: 2013-06-01
 */
class ListSelectionListenerWithPathUpdaterFactory {

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

  @NotNull
  private String getPathTextOrEmptyString(JLabel path, Object[] selectedValues) {
    return onlyOneFileIsSelected(selectedValues)
           ? getPathTextForSelectedFile(path, ((VirtualFile) selectedValues[0]).getParent())
           : "";
  }

  private boolean onlyOneFileIsSelected(Object[] selectedValues) {
    return selectedValues != null && selectedValues.length == 1;
  }

  @NotNull
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
