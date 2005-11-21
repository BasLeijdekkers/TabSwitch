package org.intellij.ideaplugins.tabswitch;

public class PreviousTabAction extends TabAction {

    public boolean isShiftDownAllowed() {
        return true;
    }

	public boolean isNoModifierDownAllowed() {
		return false;
	}
}