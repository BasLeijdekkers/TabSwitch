package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.util.NamedJDOMExternalizable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.components.ApplicationComponent;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class TabSwitchSettings implements ApplicationComponent, NamedJDOMExternalizable {

    /**
     * @noinspection PublicField,NonConstantFieldWithUpperCaseName
     * externalized in settings file
     */
    public boolean SHOW_RECENT_FILES;
    public int SCROLL_PANE_SIZE;

    public TabSwitchSettings() {
        SHOW_RECENT_FILES = false;
        SCROLL_PANE_SIZE = 20;
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "tabswitch.TabSwitchSettings";
    }

    public String getExternalFileName() {
        return "tab_switch";
    }

    public static TabSwitchSettings getInstance() {
        final Application application = ApplicationManager.getApplication();
        return application.getComponent(TabSwitchSettings.class);
    }

    public int getScrollPaneSize() {
        return SCROLL_PANE_SIZE;
    }

    public void initComponent() {
    }

    public boolean isShowRecentFiles() {
        return SHOW_RECENT_FILES;
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }
}