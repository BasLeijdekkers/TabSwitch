package org.intellij.ideaplugins.tabswitch.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intellij.ui.Gray;

/**
 * Creates the footer component. It is here where the file path is displayed - this is the container class.
 */
class FooterComponentFactory {

  JComponent create(Component pathLabel) {
    JComponent footer = newFooterJPanel();

    footer.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    if (pathLabel != null) footer.add(pathLabel);

    return footer;
  }

  private JComponent newFooterJPanel() {
    return new JPanel(new BorderLayout()) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Gray._135);
        g.drawLine(0, 0, getWidth(), 0);
      }
    };
  }
}
