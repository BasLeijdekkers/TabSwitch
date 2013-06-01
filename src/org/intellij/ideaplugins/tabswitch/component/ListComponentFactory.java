package org.intellij.ideaplugins.tabswitch.component;

import javax.swing.JLabel;
import javax.swing.JList;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBList;

/**
 * <br> User: must <br> Date: 2013-06-01
 */
public class ListComponentFactory implements ComponentFactory<JList> {

  private final Project project;

  private JLabel pathLabel;

  public ListComponentFactory(final Project project) {
    this.project = project;
  }

  @Override
  public JList create() {
    JList list = new JBList();
    list.setCellRenderer(new ListCellRendererWithColorFactory().create(project));
    list.getSelectionModel().addListSelectionListener(new ListSelectionListenerWithPathUpdaterFactory().create(list, pathLabel));
    return list;
  }

  public ListComponentFactory withPathLabel(final JLabel pathLabel) {
    this.pathLabel = pathLabel;
    return this;
  }
}
