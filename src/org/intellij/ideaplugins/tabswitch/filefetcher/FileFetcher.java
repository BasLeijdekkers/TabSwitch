package org.intellij.ideaplugins.tabswitch.filefetcher;

import java.util.List;

import com.intellij.openapi.project.Project;

/**
 * Should provide a way to fetch a list of files, of type {@code F}.
 * <pre>
 * User: must
 * Date: 2012-06-02
 * </pre>
 */
public interface FileFetcher<F> {

  /**
   * @param project an idea project.
   *
   * @return List of files of type F, or empty. Not {@code null}.
   */
  List<F> getFiles(Project project);
}
