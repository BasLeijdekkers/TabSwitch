package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class OpenFilesDialog extends DialogWrapper {

	private static final Action[] EMPTY_ACTION_ARRAY = new Action[0];

	private final JList list;
	private final int maxLength;
	private final Project project;

	OpenFilesDialog(Project project, VirtualFile[] files) {
	    super(project, false);
		this.project = project;
	    if (files.length <= 0) {
	        throw new IllegalArgumentException();
	    }
	    maxLength = files.length;
	    list = new JList(files);
	    init();
	}

	protected void init() {
		setModal(false);
		setUndecorated(true);
		setResizable(false);
		super.init();
	}

	protected Action[] createActions() {
        return EMPTY_ACTION_ARRAY;
    }

	protected JComponent createTitlePane() {
		return super.createTitlePane();
	}

	protected Border createContentPaneBorder() {
	    return BorderFactory.createEmptyBorder();
	}

    private void setSelectedIndex(int inc) {
        int index = list.getSelectedIndex() + inc;
        if (index >= maxLength) {
            index = 0;
        } else if (index < 0) {
            index = maxLength - 1;
        }
        list.setSelectedIndex(index);
    }

    public void next() {
        setSelectedIndex(1);
    }

    public void previous() {
        setSelectedIndex(-1);
    }

    public void select() {
	    final VirtualFile virtualFile = (VirtualFile)list.getSelectedValue();
	    final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
	    if (virtualFile.isValid()) {
		    fileEditorManager.openFile(virtualFile, true);
	    }
    }

    public void disposeDialog() {
        dispose();
    }

	protected JComponent createCenterPanel() {
		final JLabel label = new JLabel();
		label.setText("Open Files");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		list.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				dispose();
			}
		});
		list.setBorder(new EmptyBorder(5, 5, 5, 5));
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				dispose();
			}
		});
		list.setCellRenderer(new TabSwitchListCellRenderer(project));

		panel.add(list, BorderLayout.CENTER);
		if (!UIUtil.isMotifLookAndFeel()) {
			UIUtil.installPopupMenuBorder(panel);
		}
		UIUtil.installPopupMenuColorAndFonts(panel);
		return panel;
    }

    void centerDialog() {
        final Component parent = getOwner();
        final Point parentPoint = parent.getLocation();
        final Dimension parentDimension = parent.getSize();
        final Dimension dialogDimension = getSize();
        final int x = (int) (parentPoint.getX() +
		        (parentDimension.getWidth() - dialogDimension.getWidth()) / 2.0);
	    final int y = (int) (parentPoint.getY() +
			    (parentDimension.getHeight() - dialogDimension.getHeight()) / 2.0);
        setLocation(x, y);
    }
}