package org.tzambalayev.ideaplugins.tabswitch;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;

import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

abstract class TabAction extends AnAction {

    abstract boolean isShiftDownAllowed();

    public final void actionPerformed(AnActionEvent actionEvent) {
        final InputEvent inputEvent = actionEvent.getInputEvent();
        if (!(inputEvent instanceof KeyEvent)) {
            return;
        }
        final KeyEvent keyEvent = (KeyEvent) inputEvent;
        if (keyEvent.getID() != KeyEvent.KEY_PRESSED || !(keyEvent.isShiftDown() ^ !isShiftDownAllowed())) {
            return;
        }
        final int downModifiers = MaskUtil.getModifiers(keyEvent);
        if (downModifiers == 0 || !MaskUtil.isOneDown(downModifiers)) {
            return;
        }
        final DataContext dataContext = actionEvent.getDataContext();
        final Project project = (Project)dataContext.getData(DataConstants.PROJECT);
        final TabSelector tabSelector = new TabSelector(project);
        final FileEntry[] fileEntries = tabSelector.getFileEntries();
        if (fileEntries.length >= 1) {
            final KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(keyEvent);
            final OpenFilesDialog openFilesDialog = new OpenFilesDialog(project, tabSelector, fileEntries);
            TabSwitchApplicationComponent.getInstance().register(keyStroke, openFilesDialog);
            openFilesDialog.show();
            openFilesDialog.centerDialog();
        }
    }

}
