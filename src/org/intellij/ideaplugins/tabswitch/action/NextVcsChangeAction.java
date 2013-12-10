package org.intellij.ideaplugins.tabswitch.action;

import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcher;
import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcherChangedFilesInVcs;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * <pre>
 * User: must
 * Date: 2012-06-02
 * </pre>
 */
public class NextVcsChangeAction extends TabAction {

  private final FileFetcher<VirtualFile> fileFetcher = new FileFetcherChangedFilesInVcs();

  @NotNull
  @Override
  protected FileFetcher<VirtualFile> getFileFetcher() {
    return fileFetcher;
  }

  @Override
  protected boolean moveDownOnShow() {
    return false;
  }
}
