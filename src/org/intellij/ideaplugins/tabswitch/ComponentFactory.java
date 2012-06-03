package org.intellij.ideaplugins.tabswitch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Creates an instance of {@link JComponent}.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public interface ComponentFactory<C extends JComponent> {

  C create();

  /**
   * Creates the footer component. It is here where the file path is displayed - this is the container class.
   * <pre>
   * User: must
   * Date: 2012-05-30
   * </pre>
   */
  class FooterComponentFactory implements ComponentFactory<JComponent> {

    @Override
    public JComponent create() {
      final JComponent footer = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          g.setColor(new Color(0x87, 0x87, 0x87));
          g.drawLine(0, 0, getWidth(), 0);
        }
      };
      footer.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      return footer;
    }
  }

  class PathLabelFactory implements ComponentFactory<JLabel> {

    @Override
    public JLabel create() {
      JLabel pathLabel = new JLabel("");
      pathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      pathLabel.setFont(pathLabel.getFont().deriveFont((float) 10));
      return pathLabel;
    }
  }
}
