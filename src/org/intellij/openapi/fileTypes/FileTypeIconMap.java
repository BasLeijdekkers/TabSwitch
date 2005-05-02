package org.intellij.openapi.fileTypes;

import javax.swing.Icon;

final class FileTypeIconMap extends ResourceMap {

    private final Icon fileTypeIcon;

    private boolean readOnly;

    FileTypeIconMap(Icon fileTypeIcon) {
        this.fileTypeIcon = fileTypeIcon;
    }

    Icon getIcon(boolean readOnly) {
        this.readOnly = readOnly;
        return (Icon) getResource();
    }

    protected Object getKey() {
        return Boolean.valueOf(readOnly);
    }

    protected Object createResource() {
        Object resource;
        if (readOnly) {
            resource = new ReadOnlyFileTypeIcon(fileTypeIcon);
        } else {
            resource = fileTypeIcon;
        }
        return resource;
    }

}
