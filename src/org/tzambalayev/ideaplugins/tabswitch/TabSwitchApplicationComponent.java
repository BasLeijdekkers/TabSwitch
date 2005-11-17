package org.tzambalayev.ideaplugins.tabswitch;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;

import javax.swing.KeyStroke;
import java.awt.KeyboardFocusManager;

final class TabSwitchApplicationComponent implements ApplicationComponent {

    private final TabSwitchKeyEventDispatcher tabSwitchKeyEventProcessor = new TabSwitchKeyEventDispatcher();

    public String getComponentName() {
        return "TabSwitchApplicationComponent";
    }

    static TabSwitchApplicationComponent getInstance() {
        final Application application = ApplicationManager.getApplication();
        return application.getComponent(TabSwitchApplicationComponent.class);
    }

    void register(KeyStroke keyStroke, OpenFilesDialogInterface openFilesDialogInterface) {
        tabSwitchKeyEventProcessor.register(keyStroke, openFilesDialogInterface);
    }

    public void initComponent() {
        final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(tabSwitchKeyEventProcessor);
    }

	public void disposeComponent() {
        final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.removeKeyEventDispatcher(tabSwitchKeyEventProcessor);
    }

}