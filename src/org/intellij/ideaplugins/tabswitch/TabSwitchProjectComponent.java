/*
 * Copyright (c) 2008-2011 by Fuhrer Engineering AG, CH-2504 Biel/Bienne, Switzerland & Bas Leijdekkers
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

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.BitSet;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.intellij.ideaplugins.tabswitch.component.Components;
import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcher;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;

public class TabSwitchProjectComponent extends AbstractProjectComponent implements KeyEventDispatcher {

  private final BitSet modifiers = new BitSet();
  private final JList list;
  private final PopupChooserBuilder builder;

  private JBPopup popup;
  private int trigger = 0;
  private boolean reverse;

  public TabSwitchProjectComponent(Project project) {
    super(project);

    JLabel pathLabel = Components.newPathLabel();
    JComponent listFooter = Components.newListFooter(pathLabel);
    this.list = Components.newList(project, pathLabel);

    for (MouseMotionListener listener : list.getMouseMotionListeners()) {
      removeMouseMotionListener(listener);
    }

    this.builder = new PopupChooserBuilder(list)
      .setTitle("Open files")
      .setMovable(true)
      .setSouthComponent(listFooter)
      .setItemChoosenCallback(new Runnable() {
        @Override
        public void run() {
          onCloseOpenSelectedFile();
        }
      });
  }

  public static TabSwitchProjectComponent getHandler(Project project) {
    return project.getComponent(TabSwitchProjectComponent.class);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    boolean consumed = true;
    if (popup != null && popup.isDisposed()) {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
      consumed = false;
    } else if ((event.getID() == KeyEvent.KEY_RELEASED) && modifiers.get(event.getKeyCode())) {
      onCloseOpenSelectedFile();
    } else if (event.getID() == KeyEvent.KEY_PRESSED) {
      int keyCode = event.getKeyCode();
      if (keyCode == trigger) {
        move(event.isShiftDown());
      } else {
        switch (keyCode) {
          case KeyEvent.VK_UP:
            moveUp();
            break;
          case KeyEvent.VK_DOWN:
            moveDown();
            break;
          case KeyEvent.VK_ENTER:
            onCloseOpenSelectedFile();
            break;
          case KeyEvent.VK_SHIFT:
          case KeyEvent.VK_CONTROL:
          case KeyEvent.VK_ALT:
          case KeyEvent.VK_ALT_GRAPH:
          case KeyEvent.VK_META:
            break;
          default:
            close();
            break;
        }
      }
    }
    return consumed;
  }

  public void show(KeyEvent event, FileFetcher<VirtualFile> fileFetcher, boolean moveDownOnShow) {
    List<VirtualFile> files = fileFetcher.getFiles(myProject);
    if (files.size() <= 1) {
      return;
    }

    if (popup != null) {
      if (!popup.isVisible()) {
        popup.dispose();
      } else {
        return;
      }
    }

    popup = builder.createPopup();

    prepareListWithFiles(files);

    trigger = event.getKeyCode();

    modifiers.set(KeyEvent.VK_CONTROL, event.isControlDown());
    modifiers.set(KeyEvent.VK_META, event.isMetaDown());
    modifiers.set(KeyEvent.VK_ALT, event.isAltDown());
    modifiers.set(KeyEvent.VK_ALT_GRAPH, event.isAltGraphDown());

    reverse = event.isShiftDown();

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

    popup.showCenteredInCurrentWindow(myProject);

    if (moveDownOnShow) {
      moveDown();
    }
  }

  private void prepareListWithFiles(final List<VirtualFile> files) {
    list.setModel(new AbstractListModel() {
      @Override
      public int getSize() {
        return files.size();
      }

      @Override
      public Object getElementAt(int index) {
        return files.get(index);
      }
    });

    list.setVisibleRowCount(files.size());
  }

  private void moveUp() {
    move(true);
  }

  private void moveDown() {
    move(false);
  }

  private void move(boolean up) {
    int offset = (up ^ reverse) ? -1 : 1;
    int size = list.getModel().getSize();
    int currentIndex = (list.getSelectedIndex() + size + offset) % size;
    list.setSelectedIndex(currentIndex);
    list.ensureIndexIsVisible(currentIndex);
  }

  private void onCloseOpenSelectedFile() {
    close();
    openSelectedFile();
  }

  private void close() {
    disposePopup();
    removeMouseListeners();
    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
  }

  private void openSelectedFile() {
    VirtualFile file = (VirtualFile) list.getSelectedValue();
    if (file != null && file.isValid()) {
      FileEditorManager.getInstance(myProject).openFile(file, true, true);
    }
  }

  private void disposePopup() {
    if (popup != null) {
      popup.cancel();
      popup.dispose();
      popup = null;
    }
  }

  /**
   * Workaround for MouseListener leak added in PopupChooserBuilder.createPopup().
   */
  private void removeMouseListeners() {
    for (MouseListener listener : list.getMouseListeners()) {
      list.removeMouseListener(listener);
    }
  }

  /**
   * Remove mouse motion listener added by PopupChooserBuilder to prevent selection moving when mouse is moved over the
   * popup and TabSwitch is mapped to an Alt key combination.
   *
   * @param listener a MouseMotionListener.
   */
  private void removeMouseMotionListener(MouseMotionListener listener) {
    if (listener.getClass().getName().startsWith("com.intellij.openapi.ui.popup.PopupChooserBuilder")) {
      list.removeMouseMotionListener(listener);
    }
  }
}
