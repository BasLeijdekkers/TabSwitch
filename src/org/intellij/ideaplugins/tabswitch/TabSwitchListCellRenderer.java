package org.intellij.ideaplugins.tabswitch;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

final class TabSwitchListCellRenderer extends JLabel implements ListCellRenderer {

    private static final Color SELECTION_BACKGROUND_COLOR = new Color(166, 202, 240);
    private static final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border GRAY_LINE_BORDER = LineBorder.createGrayLineBorder();


    TabSwitchListCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        final FileEntry fileEntry = (FileEntry) value;
        setText(fileEntry.getName());
        setIcon(fileEntry.getIcon());
        setForeground(fileEntry.getStatusColor());
        final Border border;
        final Color background;
        if (isSelected) {
            border = GRAY_LINE_BORDER;
            background = SELECTION_BACKGROUND_COLOR;
        } else {
            border = EMPTY_BORDER;
            background = jList.getBackground();
        }
        setBorder(border);
        setBackground(background);
        return this;
    }
}