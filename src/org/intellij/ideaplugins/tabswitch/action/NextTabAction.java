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

import org.intellij.ideaplugins.tabswitch.filefetchers.FileFetcher;
import org.intellij.ideaplugins.tabswitch.filefetchers.FileFetcherOpenTabFiles;

import com.intellij.openapi.vfs.VirtualFile;

public class NextTabAction extends TabAction {

  private FileFetcher<VirtualFile> fileFetcher;

  @Override
  protected FileFetcher<VirtualFile> getFileFetcher() {
    if (fileFetcher == null) fileFetcher = new FileFetcherOpenTabFiles();
    return fileFetcher;
  }

  @Override
  protected boolean moveDownOnShow() {
    return true;
  }
}