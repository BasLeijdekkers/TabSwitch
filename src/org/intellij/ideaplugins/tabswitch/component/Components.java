package org.intellij.ideaplugins.tabswitch.component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import com.intellij.openapi.project.Project;

public final class Components {

  private Components() {
  }

  public static JList newList(Project project, JLabel pathLabel) {
    return new ListComponentFactory(project).create(pathLabel);
  }

  public static JComponent newListFooter(JLabel pathLabel) {
    return new FooterComponentFactory().create(pathLabel);
  }

  public static JLabel newPathLabel() {
    return new PathLabelComponentFactory().create();
  }
}
