package org.intellij.ideaplugins.tabswitch;

public class SwitchTabAction extends TabAction {

    public boolean isShiftDownAllowed() {
        return false;
    }

    public boolean isNoModifierDownAllowed() {
        return true;
    }
}