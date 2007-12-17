package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class OpenFilesDialog {


    private final JList list;
    private final int maxLength;
    private final Project project;
    private JBPopup popup;
    private boolean disposed = false;

    OpenFilesDialog(Project project, String title, VirtualFile[] files, int scrollPaneSize) {
        //super(project, title);
        this.project = project;
        list = new JList(files);
        maxLength = files.length;
	    if (scrollPaneSize > maxLength) {
		    list.setVisibleRowCount(maxLength);
	    } else {
		    list.setVisibleRowCount(scrollPaneSize);
	    }
        list.setBorder(new EmptyBorder(5, 5, 5, 5));
        list.setSelectedIndex(0);
        list.ensureIndexIsVisible(list.getSelectedIndex());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                popup.dispose();
            }
        });
        list.setCellRenderer(new TabSwitchListCellRenderer(project));
        popup = new PopupChooserBuilder(list).setTitle(title).setMovable(true).createPopup();
        //init();
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void next() {
        int index = list.getSelectedIndex() + 1;
        if (index >= maxLength) {
            index = 0;
        }
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(list.getSelectedIndex());
    }

    public void previous() {
        int index = list.getSelectedIndex() - 1;
        if (index < 0) {
            index = maxLength - 1;
        }
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(list.getSelectedIndex());
    }

    public void select() {
        final VirtualFile virtualFile = (VirtualFile)list.getSelectedValue();
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        if (virtualFile.isValid()) {
            fileEditorManager.openFile(virtualFile, true);
        }
    }

    public void show(Project project) {
        popup.showCenteredInCurrentWindow(project);
    }

    public void dispose() {
        popup.cancel();
        popup.dispose();
        disposed = true;
    }

    public boolean isVisible() {
        return popup.isVisible();
    }
}