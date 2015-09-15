package org.intellij.ideaplugins.tabswitch.action;

import org.intellij.ideaplugins.tabswitch.filefetcher.ChangedFilesInVcsFileFetcher;

public class PrevVcsChangeAction extends ChangeTabAction {

  public PrevVcsChangeAction() {
    super(new ChangedFilesInVcsFileFetcher());
  }

  @Override
  protected boolean moveUp() {
    return true;
  }
}
