package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

final class OpenFilesDialog {

    private final JList list;
    private final int maxLength;
    private final Project project;
    private JBPopup popup;
    private boolean disposed = false;

    OpenFilesDialog(Project project, String title, VirtualFile[] files, int scrollPaneSize) {
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
        final TabSwitchListener listener = new TabSwitchListener();
        list.addListSelectionListener(listener);
        list.addMouseListener(listener);
        list.setCellRenderer(new TabSwitchListCellRenderer(project));
        popup = new PopupChooserBuilder(list).setTitle(title).setMovable(true).createPopup();
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
        dispose();
    }

    public void show(Project project) {
        popup.showCenteredInCurrentWindow(project);
    }

    public void dispose() {
        popup.cancel();
        disposePopup();
        disposed = true;
    }

    private void disposePopup() {
        try{
            final Method method = JBPopup.class.getMethod("dispose");
            method.invoke(popup);
        } catch (NoSuchMethodException e) {
            // do not call the method if it does not exist:-)
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isVisible() {
        return popup.isVisible();
    }

    private class TabSwitchListener extends MouseAdapter implements ListSelectionListener {

        private int selectedIndex = 0;

        public void mouseClicked(MouseEvent event) {
            final JList list = (JList)event.getSource();
            list.setSelectedIndex(selectedIndex);
            select();
        }

        public void valueChanged(ListSelectionEvent event) {
            final JList list = (JList)event.getSource();
            final int selectedIndex = list.getSelectedIndex();
            if (selectedIndex >= 0) {
                this.selectedIndex = selectedIndex;
            }
        }
    }
}