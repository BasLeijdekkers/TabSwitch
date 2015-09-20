/*
 * Copyright (c) 2008 by Fuhrer Engineering AG, CH-2504 Biel/Bienne, Switzerland & Bas Leijdekkers
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
package org.intellij.ideaplugins.tabswitch.action;

import org.intellij.ideaplugins.tabswitch.filefetcher.OpenTabFilesFileFetcher;

public class NextTabAction extends ChangeTabAction {

  protected NextTabAction() {
    super(new OpenTabFilesFileFetcher());
  }

  @Override
  protected boolean moveOnShow() {
    return true;
  }

  @Override
  protected boolean moveUp() {
    return false;
  }
}