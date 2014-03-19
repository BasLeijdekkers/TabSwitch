package org.intellij.ideaplugins.tabswitch.action;

import java.util.List;

import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcher;
import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcherChangedFilesInVcs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class NextVcsChangeAction extends TabAction {

  private final FileFetcher<VirtualFile> fileFetcher = new FileFetcherChangedFilesInVcs();

  @Override
  protected List<VirtualFile> getOpenFiles(Project project) {
    return fileFetcher.getFiles(project);
  }

  @Override
  protected boolean moveDownOnShow() {
    return false;
  }
}
