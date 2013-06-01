package org.intellij.ideaplugins.tabswitch;

import java.awt.FontMetrics;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * <br> User: must <br> Date: 2013-06-01
 */
public class ListSelectionListenerFactoryImpl implements ListSelectionListenerFactory<ListSelectionListener> {

  @Override
  public ListSelectionListener create(final JList list, final JLabel pathLabel) {
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
    String text = "";
    final Object[] selectedValues = list.getSelectedValues();
    if ((selectedValues != null) && (selectedValues.length == 1)) {
      VirtualFile parent = ((VirtualFile) selectedValues[0]).getParent();
      if (parent != null) {
        text = parent.getPresentableUrl();
        FontMetrics fontMetrics = path.getFontMetrics(path.getFont());
        while ((fontMetrics.stringWidth(text) > path.getWidth()) && (text.indexOf(File.separatorChar, 4) > 0)) {
          text = "..." + text.substring(text.indexOf(File.separatorChar, 4));
        }
      }
    }
    path.setText(text);
  }
}
