package org.tzambalayev.ideaplugins.tabswitch;

import java.awt.KeyEventDispatcher;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

final class TabSwitchKeyEventDispatcher implements KeyEventDispatcher {

    private OpenFilesDialogInterface openFilesDialogInterface;
    private int mainKeyCode;
    private int mainModifiers;
    private int downKeyCode;

    private static int downMaskToKeyCode(int downMask) {
        int keyCode;
        if ((downMask & InputEvent.CTRL_MASK) != 0) {
            keyCode = KeyEvent.VK_CONTROL;
        } else if ((downMask & InputEvent.ALT_MASK) != 0) {
            keyCode = KeyEvent.VK_ALT;
        } else if ((downMask & InputEvent.META_MASK) != 0) {
            keyCode = KeyEvent.VK_META;
        } else if ((downMask & InputEvent.ALT_GRAPH_MASK) != 0) {
            keyCode = KeyEvent.VK_ALT_GRAPH;
        } else {
            throw new RuntimeException();
        }
        return keyCode;
    }

    void register(KeyStroke mainKeyStroke, OpenFilesDialogInterface openFilesDialogInterface) {
        mainKeyCode = mainKeyStroke.getKeyCode();
        mainModifiers = mainKeyStroke.getModifiers();
        downKeyCode = downMaskToKeyCode(mainModifiers);
        this.openFilesDialogInterface = openFilesDialogInterface;
    }

    private void dispose() {
        openFilesDialogInterface.disposeDialog();
        openFilesDialogInterface = null;
        mainKeyCode = -1;
        mainModifiers = -1;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (openFilesDialogInterface != null) {
            int id = keyEvent.getID();
            boolean pressed = id == KeyEvent.KEY_PRESSED;
            boolean released = id == KeyEvent.KEY_RELEASED;
            if (pressed || released) {
                int keyCode = keyEvent.getKeyCode();
                if (keyCode == mainKeyCode) {
                    if (pressed) {
                        if (MaskUtil.getModifiers(mainModifiers) == MaskUtil.getModifiers(keyEvent)) {
                            if (keyEvent.isShiftDown()) {
                                openFilesDialogInterface.previous();
                            } else {
                                openFilesDialogInterface.next();
                            }
                        }
                    }
                } else if (keyCode == downKeyCode) {
                    if (released) {
                        openFilesDialogInterface.select();
                        dispose();
                    }
                } else if (keyCode == KeyEvent.VK_SHIFT) {
                    // no op
                } else {
                    dispose();
                }
            }
        }
        return false;
    }

}
