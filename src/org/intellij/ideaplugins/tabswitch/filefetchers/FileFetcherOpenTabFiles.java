package org.intellij.ideaplugins.tabswitch.filefetchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
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

  @Override
  public List<VirtualFile> getFiles(final Project project) {
    List<VirtualFile> result = new ArrayList<VirtualFile>();
    FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
    VirtualFile[] files = EditorHistoryManager.getInstance(project).getFiles();
    for (VirtualFile file : files) {
      if (fileEditorManager.isFileOpen(file)) {
        result.add(file);
      }
    }
    Collections.reverse(result);
    return result;
  }
}
