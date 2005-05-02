package org.tzambalayev.ideaplugins.tabswitch;

import java.awt.Color;
import javax.swing.Icon;

final class FileEntry {

    private final Icon icon;
    private final Color statusColor;
    private final String name;

    FileEntry(String name, Icon icon, Color statusColor) {
        this.name = name;
        this.icon = icon;
        this.statusColor = statusColor;
    }

    Icon getIcon() {
        return icon;
    }

    String getName() {
        return name;
    }

    Color getStatusColor() {
        return statusColor;
    }

}
