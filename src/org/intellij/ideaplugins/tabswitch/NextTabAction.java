package org.intellij.ideaplugins.tabswitch;

public class NextTabAction extends TabAction {

    public boolean isShiftDownAllowed() {
        return false;
    }

	public boolean isNoModifierDownAllowed() {
		return false;
	}
}