package org.tzambalayev.ideaplugins.tabswitch;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

final class OpenFilesDialog extends DialogWrapper implements OpenFilesDialogInterface {

    private final JList jList;
    private final int maxLength;
    private final TabSelectorInterface tabSelectorInterface;

    OpenFilesDialog(Project project, TabSelectorInterface tabSelectorInterface, FileEntry[] fileEntries) {
        super(project, false);
        this.tabSelectorInterface = tabSelectorInterface;
        if (fileEntries.length <= 0) {
            throw new IllegalArgumentException();
        }
        maxLength = fileEntries.length;
        jList = new JList(fileEntries);
        setModal(false);
        setUndecorated(true);
        init();
    }

    protected Action[] createActions() {
        return new Action[0];
    }

    protected Border createContentPaneBorder() {
        return new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.GRAY, Color.WHITE, new Color(216, 216, 216));
    }

    private void setSelectedIndex(int inc) {
        int index = jList.getSelectedIndex() + inc;
        if (index >= maxLength) {
            index = 0;
        } else if (index < 0) {
            index = maxLength - 1;
        }
        jList.setSelectedIndex(index);
    }

    public void next() {
        setSelectedIndex(1);
    }

    public void previous() {
        setSelectedIndex(-1);
    }

    public void select() {
        tabSelectorInterface.selected(jList.getSelectedIndex());
    }

    public void disposeDialog() {
        dispose();
    }

    protected JComponent createCenterPanel() {
        jList.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                dispose();
            }
        });
        jList.setBorder(new EmptyBorder(4, 4, 4, 4));
        jList.setSelectedIndex(0);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                dispose();
            }
        });
        jList.setCellRenderer(new TabSwitchListCellRenderer());
        return jList;
    }

    void centerDialog() {
        Component parent = getOwner();
        Point parentPoint = parent.getLocation();
        Dimension parentDimension = parent.getSize();
        Dimension dialogDimension = getSize();
        int x = (int) (parentPoint.getX() + (parentDimension.getWidth() - dialogDimension.getWidth()) / 2);
        int y = (int) (parentPoint.getY() + (parentDimension.getHeight() - dialogDimension.getHeight()) / 2);
        setLocation(x, y);
    }

}
