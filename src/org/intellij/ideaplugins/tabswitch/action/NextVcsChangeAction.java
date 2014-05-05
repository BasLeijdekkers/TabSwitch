package org.intellij.ideaplugins.tabswitch.action;

import java.util.List;

import org.intellij.ideaplugins.tabswitch.filefetcher.ChangedFilesInVcsFileFetcher;
import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcher;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class NextVcsChangeAction extends TabAction {

  private final FileFetcher<VirtualFile> fileFetcher = new ChangedFilesInVcsFileFetcher();

  @Override
  protected List<VirtualFile> getOpenFiles(Project project) {
    return fileFetcher.getFiles(project);
  }

  @Override
  protected boolean moveDownOnShow() {
    return false;
  }
}
