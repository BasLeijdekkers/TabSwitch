package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

public abstract class IdeaDialog extends DialogWrapper {

    private static final Action[] EMPTY_ACTION_ARRAY = new Action[0];

    private JLabel label = null;

    protected IdeaDialog(Project project, boolean canBeParent) {
        super(project, canBeParent);
    }

    protected IdeaDialog(Project project, String title) {
        super(project, false);
        if (title != null) {
            label = new JLabel(title);
        }
    }

    protected IdeaDialog(Project project) {
        super(project, false);
    }

    private void centerDialog() {
        final Component parent = getOwner();
        final Rectangle parentBounds = parent.getBounds();
        final Dimension dialogDimension = getSize();
        final int x = parentBounds.x +
                (parentBounds.width - dialogDimension.width >> 1);
        final int y = parentBounds.y +
                (parentBounds.height - dialogDimension.height >> 1);
        setLocation(x, y);
    }

    protected Action[] createActions() {
        return EMPTY_ACTION_ARRAY;
    }

    protected Border createContentPaneBorder() {
        return BorderFactory.createEmptyBorder();
    }

    protected JComponent createTitlePane() {
        if (label == null) {
            return super.createTitlePane();
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    protected void init() {
        setModal(false);
        setUndecorated(true);
        setResizable(false);
        super.init();
        final Container contentPane = getContentPane();
        final LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (!"Motif".equals(lookAndFeel.getID())) {
            LookAndFeel.installBorder((JComponent)contentPane, "PopupMenu.border");
        }
        LookAndFeel.installColorsAndFont((JComponent)contentPane, "PopupMenu.background",
                "PopupMenu.foreground", "PopupMenu.font");
    }

    public void show() {
        super.show();
        centerDialog();
    }
}