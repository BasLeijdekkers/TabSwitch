package org.intellij.ideaplugins.tabswitch.action;

import org.intellij.ideaplugins.tabswitch.filefetcher.ChangedFilesInVcsFileFetcher;

public class NextVcsChangeAction extends ChangeTabAction {

  public NextVcsChangeAction() {
    super(new ChangedFilesInVcsFileFetcher());
  }

  @Override
  protected boolean moveUp() {
    return false;
  }
}
