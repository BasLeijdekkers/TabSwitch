/**
 * (c) 2005 Carp Technologies BV
 * Hengelosestraat 705, 7521PA Enschede
 * Created: Dec 28, 2005, 11:38:51 AM
 */
package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

/**
 * @author <A href="bas@carp-technologies.nl">Bas Leijdekkers</a>
 */
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
        final Point parentPoint = parent.getLocation();
        final Dimension parentDimension = parent.getSize();
        final Dimension dialogDimension = getSize();
        final int x = (int)(parentPoint.getX() +
                (parentDimension.getWidth() - dialogDimension.getWidth()) / 2.0);
        final int y = (int)(parentPoint.getY() +
                (parentDimension.getHeight() - dialogDimension.getHeight()) / 2.0);
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

    public void dispose() {
        super.dispose();
    }

    protected void init() {
        setModal(false);
        setUndecorated(true);
        setResizable(false);
        super.init();
        final Container contentPane = getContentPane();
        if (!UIUtil.isMotifLookAndFeel()) {
            UIUtil.installPopupMenuBorder((JComponent)contentPane);
        }
        UIUtil.installPopupMenuColorAndFonts((JComponent)contentPane);
    }

    public void show() {
        super.show();
        centerDialog();
    }
}