/*
 * Copyright 2008 Bas Leijdekkers
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
package org.intellij.ideaplugins.tabswitch;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public class SwitchTabAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());
        final TabSwitchSettings tabSwitchSettings = TabSwitchSettings.getInstance();
        final UISettings uiSettings = UISettings.getInstance();
        final int editorTabLimit = uiSettings.EDITOR_TAB_LIMIT;
        final boolean showRecentFiles = tabSwitchSettings.SHOW_RECENT_FILES ||
                editorTabLimit == 1;
        final VirtualFile file = getFile(project, showRecentFiles);
        if (file != null && file.isValid()) {
            FileEditorManager.getInstance(project).openFile(file, true);
        }
    }

    @Nullable
    private static VirtualFile getFile(Project project, boolean showRecentFiles) {
        final FileEditorManager manager = FileEditorManager.getInstance(project);
        final VirtualFile[] files = EditorHistoryManager.getInstance(project).getFiles();
        for (int i = files.length - 2; i >= 0; i--) {
            final VirtualFile file = files[i];
            if (showRecentFiles || manager.isFileOpen(file)) {
                return file;
            }
        }
        return null;
    }
}