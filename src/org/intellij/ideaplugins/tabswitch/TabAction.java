package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public abstract class TabAction extends AnAction {

    public abstract boolean isShiftDownAllowed();

	public abstract boolean isNoModifierDownAllowed();

    public final void actionPerformed(AnActionEvent actionEvent) {
        final InputEvent inputEvent = actionEvent.getInputEvent();
        if (!(inputEvent instanceof KeyEvent)) {
            return;
        }
        final KeyEvent keyEvent = (KeyEvent) inputEvent;
        if (keyEvent.getID() != KeyEvent.KEY_PRESSED ||
		        !(keyEvent.isShiftDown() ^ !isShiftDownAllowed())) {
            return;
        }
        final int downModifiers = MaskUtil.getModifiers(keyEvent);
        if (!isNoModifierDownAllowed()) {
	        if (downModifiers == 0 || !MaskUtil.isOneDown(downModifiers)) {
	            return;
	        }
        }
        final DataContext dataContext = actionEvent.getDataContext();
        final Project project = (Project)dataContext.getData(DataConstants.PROJECT);
	    if (project == null) {
		    return;
	    }
	    final TabSwitchProjectComponent tabSwitchProjectComponent =
			    project.getComponent(TabSwitchProjectComponent.class);
	    final VirtualFile[] files = tabSwitchProjectComponent.getFiles();
        if (files.length >= 1) {
            final KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(keyEvent);
            final OpenFilesDialog openFilesDialog = new OpenFilesDialog(project, files);
            TabSwitchProjectComponent.register(keyStroke, openFilesDialog);
	        if(keyEvent.getModifiers() != 0) {
		        openFilesDialog.show();
		        openFilesDialog.centerDialog();
	        }
        }
    }
}