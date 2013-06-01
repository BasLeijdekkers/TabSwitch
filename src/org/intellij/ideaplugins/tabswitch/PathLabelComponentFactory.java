package org.intellij.ideaplugins.tabswitch;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * <br> User: must <br> Date: 2013-06-01
 */
public class PathLabelComponentFactory implements ComponentFactory<JLabel> {

  @Override
  public JLabel create() {
    JLabel pathLabel = new JLabel("");
    pathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    pathLabel.setFont(pathLabel.getFont().deriveFont((float) 10));
    return pathLabel;
  }
}
