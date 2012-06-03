package org.intellij.ideaplugins.tabswitch.action;

import org.intellij.ideaplugins.tabswitch.filefetchers.FileFetcher;
import org.intellij.ideaplugins.tabswitch.filefetchers.FileFetcherChangedFilesInVcs;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * <pre>
 * User: must
 * Date: 2012-06-02
 * </pre>
 */
public class NextVcsChangeAction extends TabAction {

  private FileFetcher<VirtualFile> fileFetcher;

  @Override
  protected FileFetcher<VirtualFile> getFileFetcher() {
    if (fileFetcher == null) fileFetcher = new FileFetcherChangedFilesInVcs();
    return fileFetcher;
  }

  @Override
  protected boolean moveDownOnShow() {
    return false;
  }
}
