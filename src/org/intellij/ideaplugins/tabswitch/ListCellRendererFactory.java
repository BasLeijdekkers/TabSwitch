package org.intellij.ideaplugins.tabswitch;

import javax.swing.ListCellRenderer;

import com.intellij.openapi.project.Project;

/**
 * Creates a {@link ListCellRenderer} for the project.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public interface ListCellRendererFactory<R extends ListCellRenderer> {

  R create(Project project);
}
