/*
 * Copyright 2008 Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.ideaplugins.tabswitch;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.NamedJDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;

public class TabSwitchSettings implements ApplicationComponent, NamedJDOMExternalizable {

  /**
   * @noinspection PublicField, NonConstantFieldWithUpperCaseName externalized in settings file
   */
  public boolean SHOW_RECENT_FILES;

  public TabSwitchSettings() {
    this.SHOW_RECENT_FILES = false;
  }

  public static TabSwitchSettings getInstance() {
    return ApplicationManager.getApplication().getComponent(TabSwitchSettings.class);
  }

  @Override
  public void disposeComponent() {
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "intellij.ideaplugins.tabswitch.TabSwitchSettings";
  }

  @Override
  public String getExternalFileName() {
    return "tab_switch";
  }

  @Override
  public void initComponent() {
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    DefaultJDOMExternalizer.readExternal(this, element);
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    DefaultJDOMExternalizer.writeExternal(this, element);
  }
}