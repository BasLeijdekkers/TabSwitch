package org.intellij.ideaplugins.tabswitch;

import javax.swing.KeyStroke;
import java.awt.KeyEventDispatcher;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

final class TabSwitchKeyEventDispatcher implements KeyEventDispatcher {

    private OpenFilesDialog openFilesDialog = null;
    private int mainKeyCode = 0;
    private int mainModifiers = 0;
    private int downKeyCode = 0;

    private static int downMaskToKeyCode(int downMask) {
        final int keyCode;
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

    void register(KeyStroke mainKeyStroke, OpenFilesDialog openFilesDialog) {
        mainKeyCode = mainKeyStroke.getKeyCode();
        mainModifiers = mainKeyStroke.getModifiers();
        if (mainModifiers != 0) {
            downKeyCode = downMaskToKeyCode(mainModifiers);
        }
        this.openFilesDialog = openFilesDialog;
    }

    private void reset() {
        openFilesDialog = null;
        mainKeyCode = 0;
        mainModifiers = 0;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (openFilesDialog == null) {
            return false;
        }
        final int id = keyEvent.getID();
        final boolean pressed = id == KeyEvent.KEY_PRESSED;
        final boolean released = id == KeyEvent.KEY_RELEASED;
        if (!pressed && !released) {
            return false;
        }
        final int keyCode = keyEvent.getKeyCode();
        if (keyCode == mainKeyCode) {
            if (pressed) {
                if (mainModifiers == 0) {
                    openFilesDialog.next();
                    openFilesDialog.select();
                    reset();
                } else if (MaskUtil.getModifiers(mainModifiers) ==
                        MaskUtil.getModifiers(keyEvent)) {
                    if (keyEvent.isShiftDown()) {
                        openFilesDialog.previous();
                    } else {
                        openFilesDialog.next();
                    }
                }
            }
        } else if (keyCode == downKeyCode) {
            if (released && openFilesDialog.isVisible()) {
                openFilesDialog.select();
                reset();
            }
        } else if (keyCode == KeyEvent.VK_SHIFT) {
            // no op
        } else {
            reset();
        }
        return false;
    }
}