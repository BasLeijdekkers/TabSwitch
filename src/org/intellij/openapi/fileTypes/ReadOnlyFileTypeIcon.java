package org.intellij.openapi.fileTypes;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;

import com.intellij.util.ImageLoader;

final class ReadOnlyFileTypeIcon implements Icon {

    private static final Image LOCKED_IMAGE = ImageLoader.loadFromResource("/nodes/locked.png");
    private static final int LOCKED_IMAGE_WIDTH = LOCKED_IMAGE.getWidth(null);
    private static final int LOCKED_IMAGE_HEIGHT = LOCKED_IMAGE.getHeight(null);

    private final Icon fileTypeIcon;
    private final int iconWidth;
    private final int iconHeight;

    ReadOnlyFileTypeIcon(Icon fileTypeIcon) {
        this.fileTypeIcon = fileTypeIcon;
        iconWidth = Math.max(fileTypeIcon.getIconWidth(), LOCKED_IMAGE_WIDTH);
        iconHeight = Math.max(fileTypeIcon.getIconHeight(), LOCKED_IMAGE_HEIGHT);
    }

    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        fileTypeIcon.paintIcon(component, graphics, x, y);
        graphics.drawImage(LOCKED_IMAGE, x, y, null);
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

}
