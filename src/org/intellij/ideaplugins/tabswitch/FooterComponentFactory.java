package org.intellij.ideaplugins.tabswitch;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intellij.ui.Gray;

/**
 * Creates the footer component. It is here where the file path is displayed - this is the container class.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public class FooterComponentFactory implements ComponentFactory<JComponent> {

  private JLabel pathLabel;

  @Override
  public JComponent create() {
    JComponent footer = new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Gray._135);
        g.drawLine(0, 0, getWidth(), 0);
      }
    };

    footer.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    if (pathLabel != null) footer.add(pathLabel);

    return footer;
  }

  public FooterComponentFactory withPathLabel(final JLabel pathLabel) {
    this.pathLabel = pathLabel;
    return this;
  }
}
