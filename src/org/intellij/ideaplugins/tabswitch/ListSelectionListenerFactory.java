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
 * Creates a {@link ListSelectionListener}. Updates the path to currently selected file in the popup window.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public interface ListSelectionListenerFactory<S extends ListSelectionListener> {

  S create(JList list, JLabel pathLabel);

  class ListSelectionListenerFactoryImpl implements ListSelectionListenerFactory<ListSelectionListener> {

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

    private void updatePath(final JList list, final JLabel path) {
      String text = "";
      final Object[] selectedValues = list.getSelectedValues();
      if ((selectedValues != null) && (selectedValues.length == 1)) {
        final VirtualFile parent = ((VirtualFile) selectedValues[0]).getParent();
        if (parent != null) {
          text = parent.getPresentableUrl();
          final FontMetrics fontMetrics = path.getFontMetrics(path.getFont());
          while ((fontMetrics.stringWidth(text) > path.getWidth()) && (text.indexOf(File.separatorChar, 4) > 0)) {
            text = "..." + text.substring(text.indexOf(File.separatorChar, 4));
          }
        }
      }
      path.setText(text);
    }

  }

}
