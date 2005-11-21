package org.intellij.ideaplugins.tabswitch;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

final class MaskUtil {

    private MaskUtil() {}

	private static final int[] DOWN_MASKS = {
        InputEvent.CTRL_MASK,
        InputEvent.ALT_MASK,
        InputEvent.META_MASK,
        InputEvent.ALT_GRAPH_MASK,
    };

    private static final int DOWN_MASK;

    static {
        int mask = 0;
        for (int i = 0; i < DOWN_MASKS.length; i++) {
            mask |= DOWN_MASKS[i];
        }
        DOWN_MASK = mask;
    }

    static int getModifiers(KeyEvent keyEvent) {
        return getModifiers(keyEvent.getModifiers());
    }

    static int getModifiers(int modifiers) {
        return modifiers & DOWN_MASK;
    }

    static boolean isOneDown(int downModifiers) {
        boolean oneDown = false;
        for (int i = 0; i < DOWN_MASKS.length; i++) {
            if (downModifiers == DOWN_MASKS[i]) {
                oneDown = true;
                break;
            }
        }
        return oneDown;
    }
}