/*
 * Copyright (c) 2008-2009 by Fuhrer Engineering AG, CH-2504 Biel/Bienne, Switzerland & Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.ideaplugins.tabswitch.action;

import java.awt.event.KeyEvent;
import java.util.List;

import org.intellij.ideaplugins.tabswitch.TabSwitchProjectComponent;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public abstract class TabAction extends AnAction implements DumbAware {

  protected abstract List<VirtualFile> getOpenFiles(Project project);

  /**
   * @return true if to move down selected index position one step on show of list popup chooser window.
   */
  protected abstract boolean moveOnShow();

  /**
   * @return true if this action moves down the list, false - up the list
   */
  protected abstract boolean moveUp();

  @Override
  public void actionPerformed(AnActionEvent event) {
    Project project = getProject(event);
    if (canShowTabSwitchPopup(event, project)) {
      TabSwitchProjectComponent.getHandler(project).show(getKeyEvent(event), moveUp(), moveOnShow(), getOpenFiles(project));
    }
  }

  @Override
  public void update(AnActionEvent event) {
    event.getPresentation().setEnabled(getProject(event) != null);
  }

  private Project getProject(AnActionEvent event) {
    return PlatformDataKeys.PROJECT.getData(event.getDataContext());
  }

  private boolean canShowTabSwitchPopup(AnActionEvent event, Project project) {
    return project != null && event.getInputEvent() instanceof KeyEvent;
  }

  private KeyEvent getKeyEvent(AnActionEvent event) {
    return (KeyEvent) event.getInputEvent();
  }
}