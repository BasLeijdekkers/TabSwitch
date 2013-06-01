/*
 * Copyright 2008-2011 Bas Leijdekkers
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

import org.intellij.ideaplugins.tabswitch.TabSwitchSettings;
import org.jetbrains.annotations.Nullable;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class SwitchTabAction extends AnAction implements DumbAware {

  @Override
  public void actionPerformed(AnActionEvent event) {
    Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());
    FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
    VirtualFile file = getFileOrNull(project, fileEditorManager);
    if (file != null && file.isValid()) {
      fileEditorManager.openFile(file, true, true);
    }
  }

  @Nullable
  private VirtualFile getFileOrNull(Project project, final FileEditorManager fileEditorManager) {
    final boolean showRecentFiles = canShowRecentFiles();
    VirtualFile[] files = EditorHistoryManager.getInstance(project).getFiles();
    for (int i = files.length - 2; i >= 0; i--) {
      VirtualFile file = files[i];
      if (showRecentFiles || fileEditorManager.isFileOpen(file)) {
        return file;
      }
    }
    return null;
  }

  private boolean canShowRecentFiles() {
    return TabSwitchSettings.getInstance().SHOW_RECENT_FILES || UISettings.getInstance().EDITOR_TAB_LIMIT == 1;
  }
}