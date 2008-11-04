/*
 * Copyright (c) 2008 by Fuhrer Engineering AG, CH-2504 Biel/Bienne, Switzerland & Bas Leijdekkers
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

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class TabAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = PlatformDataKeys.PROJECT.getData(event.getDataContext());
        if ((project != null) && (event.getInputEvent() instanceof KeyEvent)) {
            final TabSwitchSettings tabSwitchSettings = TabSwitchSettings.getInstance();
            final UISettings uiSettings = UISettings.getInstance();
            final int editorTabLimit = uiSettings.EDITOR_TAB_LIMIT;
            final boolean showRecentFiles = tabSwitchSettings.SHOW_RECENT_FILES ||
                    editorTabLimit == 1;
            final List<VirtualFile> files = getFiles(project, showRecentFiles);
            if (!files.isEmpty()) {
                new Handler(project, files, (KeyEvent) event.getInputEvent(), isReverse(),
                        showRecentFiles).show();
            }
        }
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabled(
                PlatformDataKeys.PROJECT.getData(event.getDataContext()) != null);
    }

    abstract boolean isReverse();

    private static List<VirtualFile> getFiles(Project project, boolean showRecentFiles) {
        final List<VirtualFile> result = new ArrayList();
        final FileEditorManager manager = FileEditorManager.getInstance(project);
        final VirtualFile[] files = EditorHistoryManager.getInstance(project).getFiles();
        for (VirtualFile file : files) {
            if (showRecentFiles || manager.isFileOpen(file)) {
                result.add(file);
            }
        }
        Collections.reverse(result);
        return result;
    }
}