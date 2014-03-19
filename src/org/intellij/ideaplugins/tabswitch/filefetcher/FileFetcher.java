package org.intellij.ideaplugins.tabswitch.filefetcher;

import java.util.List;

import com.intellij.openapi.project.Project;

/**
 * Should provide a way to fetch a list of files, of type {@code FILE}.
 */
public interface FileFetcher<FILE> {
  /**
   * @param project an idea project.
   *
   * @return List of files of type {@code FILE}, or empty. Not {@code null}.
   */
  List<FILE> getFiles(Project project);
}
