package org.intellij.ideaplugins.tabswitch.component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;

/**
 * <br> User: must <br> Date: 2013-12-10
 */
public final class Components {

  private Components() {
  }

  @NotNull
  public static JList newList(Project project, JLabel pathLabel) {
    return new ListComponentFactory(project).create(pathLabel);
  }

  @NotNull
  public static JComponent newListFooter(JLabel pathLabel) {
    return new FooterComponentFactory().create(pathLabel);
  }

  @NotNull
  public static JLabel newPathLabel() {
    return new PathLabelComponentFactory().create();
  }
}
