package org.intellij.ideaplugins.tabswitch.filefetcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Creates a list of {@link VirtualFile} by fetching all the files that are open in tabs in current project.
 * <pre>
 * User: must
 * Date: 2012-06-02
 * </pre>
 */
public class FileFetcherOpenTabFiles implements FileFetcher<VirtualFile> {

  @NotNull
  @Override
  public List<VirtualFile> getFiles(Project project) {
    List<VirtualFile> result = Arrays.asList(FileEditorManager.getInstance(project).getOpenFiles());
    Collections.reverse(result);
    return result;
  }
}
