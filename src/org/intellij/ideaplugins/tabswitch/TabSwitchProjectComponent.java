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

import org.intellij.ideaplugins.tabswitch.component.FooterComponentFactory;
import org.intellij.ideaplugins.tabswitch.component.PathLabelComponentFactory;
import org.intellij.ideaplugins.tabswitch.filefetcher.FileFetcher;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;

public class TabSwitchProjectComponent extends AbstractProjectComponent implements KeyEventDispatcher {

  private final JList list;
  private final PopupChooserBuilder builder;
  private final BitSet modifiers = new BitSet();

  private JBPopup popup;
  private int trigger = 0;
  private boolean reverse;

  public TabSwitchProjectComponent(final Project project) {
    super(project);

    JLabel pathLabel = new PathLabelComponentFactory().create();

    this.list = new JList();
    list.setCellRenderer(new ListCellRendererWithColorFactory().create(project));
    list.getSelectionModel().addListSelectionListener(new ListSelectionListenerWithPathUpdaterFactory().create(list, pathLabel));

    this.builder = new PopupChooserBuilder(list).setTitle("Open files");

    for (MouseMotionListener listener : list.getMouseMotionListeners()) {
      removeMouseMotionListener(listener);
    }

    JComponent footerComponent = new FooterComponentFactory()
      .withPathLabel(pathLabel)
      .create();

    builder
      .setMovable(true)
      .setSouthComponent(footerComponent)
      .setItemChoosenCallback(new Runnable() {
        @Override
        public void run() {
          close(true);
        }
      });
  }

  public static TabSwitchProjectComponent getHandler(final Project project) {
    return project.getComponent(TabSwitchProjectComponent.class);
  }

  @Override
  public boolean dispatchKeyEvent(final KeyEvent event) {
    boolean consumed = true;
    if (popup != null && popup.isDisposed()) {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
      consumed = false;
    } else if ((event.getID() == KeyEvent.KEY_RELEASED) && modifiers.get(event.getKeyCode())) {
      close(true);
    } else if (event.getID() == KeyEvent.KEY_PRESSED) {
      final int keyCode = event.getKeyCode();
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
            close(true);
            break;
          case KeyEvent.VK_SHIFT:
          case KeyEvent.VK_CONTROL:
          case KeyEvent.VK_ALT:
          case KeyEvent.VK_ALT_GRAPH:
          case KeyEvent.VK_META:
            break;
          default:
            close(false);
            break;
        }
      }
    }
    return consumed;
  }

  public void show(final KeyEvent event, final FileFetcher<VirtualFile> fileFetcher, final boolean moveDownOnShow) {
    final List<VirtualFile> files = fileFetcher.getFiles(myProject);
    if (files.isEmpty()) {
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
    trigger = event.getKeyCode();
    modifiers.set(KeyEvent.VK_CONTROL, event.isControlDown());
    modifiers.set(KeyEvent.VK_META, event.isMetaDown());
    modifiers.set(KeyEvent.VK_ALT, event.isAltDown());
    modifiers.set(KeyEvent.VK_ALT_GRAPH, event.isAltGraphDown());
    this.reverse = event.isShiftDown();
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    popup.showCenteredInCurrentWindow(myProject);

    if (moveDownOnShow) moveDown();
  }

  private void moveUp() {
    move(true);
  }

  private void moveDown() {
    move(false);
  }

  private void move(boolean up) {
    final int offset = (up ^ reverse) ? -1 : 1;
    final int size = list.getModel().getSize();
    final int currentIndex = (list.getSelectedIndex() + size + offset) % size;
    list.setSelectedIndex(currentIndex);
    list.ensureIndexIsVisible(currentIndex);
  }

  private void close(boolean openFile) {
    if (popup != null) {
      popup.cancel();
      popup.dispose();
      popup = null;
    }

    removeMouseListeners();

    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
    if (openFile) {
      VirtualFile file = (VirtualFile) list.getSelectedValue();
      if (file.isValid()) {
        FileEditorManager.getInstance(myProject).openFile(file, true, true);
      }
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
  private void removeMouseMotionListener(final MouseMotionListener listener) {
    if (listener.getClass().getName().startsWith("com.intellij.openapi.ui.popup.PopupChooserBuilder")) {
      list.removeMouseMotionListener(listener);
    }
  }
}
