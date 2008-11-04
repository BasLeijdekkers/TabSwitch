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
package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.IconUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.BitSet;
import java.util.List;

class Handler implements KeyEventDispatcher {

    private final Project project;

    private final JList list;

    private final JBPopup popup;

    private final int trigger;

    private final BitSet modifiers = new BitSet();

    private final boolean reverse;

    Handler(Project project, final List<VirtualFile> fileList, KeyEvent event, boolean reverse,
            boolean showRecentFiles) {
        this.project = project;

        final JLabel path = new JLabel(" ");
        path.setHorizontalAlignment(SwingConstants.RIGHT);
        path.setFont(path.getFont().deriveFont((float) 10));
        final JComponent footer = buildFooter(path);
        list = new JList(new SimpleListModel(fileList));
        list.setCellRenderer(getRenderer(project));
        list.getSelectionModel().addListSelectionListener(getListener(list, path));

        final PopupChooserBuilder builder = new PopupChooserBuilder(list);
        if (showRecentFiles) {
            builder.setTitle("Recent Files");
        } else {
            builder.setTitle("Open Files");
        }
        builder.setMovable(true);
        builder.setSouthComponent(footer);
        builder.setItemChoosenCallback(new Runnable() {
            public void run() {
                close(true);
            }
        });
        popup = builder.createPopup();
        trigger = event.getKeyCode();
        modifiers.set(KeyEvent.VK_CONTROL, event.isControlDown());
        modifiers.set(KeyEvent.VK_META, event.isMetaDown());
        modifiers.set(KeyEvent.VK_ALT, event.isAltDown());
        modifiers.set(KeyEvent.VK_ALT_GRAPH, event.isAltGraphDown());
        this.reverse = reverse ^ event.isShiftDown();
    }

    private static JComponent buildFooter(Component footerComponent) {
        final JPanel footer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0x87, 0x87, 0x87));
                g.drawLine(0, 0, getWidth(), 0);
            }
        };
        footer.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        footer.add(footerComponent);
        return footer;
    }

    private void close(boolean open) {
        popup.cancel();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
        if (open) {
            final VirtualFile file = (VirtualFile) list.getSelectedValue();
            if (file.isValid()) {
                FileEditorManager.getInstance(project).openFile(file, true);
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean consumed = true;
        if (popup.isDisposed()) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
            consumed = false;
        } else if ((event.getID() == KeyEvent.KEY_RELEASED) &&
                modifiers.get(event.getKeyCode())) {
            close(true);
        } else if (event.getID() == KeyEvent.KEY_PRESSED) {
            final int keyCode = event.getKeyCode();
            if (event.getKeyCode() == trigger) {
                move(event.isShiftDown());
            } else {
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        move(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        move(false);
                        break;
                    case KeyEvent.VK_ENTER:
                        close(true);
                        break;
                    case KeyEvent.VK_SHIFT:
                        break;
                    case KeyEvent.VK_CONTROL:
                        break;
                    case KeyEvent.VK_ALT:
                        break;
                    case KeyEvent.VK_ALT_GRAPH:
                        break;
                    case KeyEvent.VK_META:
                        break;
                    default:
                        close(false);
                        break;
            }}
        }
        return consumed;
    }

    private static ListSelectionListener getListener(final JList list, final JLabel path) {
        return new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        update(list, path);
                    }
                });
            }
        };
    }

    static ListCellRenderer getRenderer(final Project project) {
        return new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(JList list, Object value, int index,
                                                 boolean selected, boolean hasFocus) {
                if (value instanceof VirtualFile) {
                    final VirtualFile file = (VirtualFile) value;
                    setIcon(IconUtil.getIcon(file, Iconable.ICON_FLAG_READ_STATUS, project));
                    final FileStatus status =
                            FileStatusManager.getInstance(project).getStatus(file);
                    final TextAttributes attributes =
                            new TextAttributes(status.getColor(), null, null,
                                    EffectType.LINE_UNDERSCORE, Font.PLAIN);
                    append(file.getName(), SimpleTextAttributes.fromTextAttributes(attributes));
                }
            }
        };
    }

    private void move(boolean up) {
        final int offset = (up ^ reverse) ? -1 : 1;
        final int size = list.getModel().getSize();
        list.setSelectedIndex((list.getSelectedIndex() + size + offset) % size);
        list.ensureIndexIsVisible(list.getSelectedIndex());
    }

    void show() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        popup.showCenteredInCurrentWindow(project);
    }

    private static void update(JList list, JLabel path) {
        String text = " ";
        final Object[] values = list.getSelectedValues();
        if ((values != null) && (values.length == 1)) {
            final VirtualFile parent = ((VirtualFile) values[0]).getParent();
            if (parent != null) {
                text = parent.getPresentableUrl();
                final FontMetrics metrics = path.getFontMetrics(path.getFont());
                while ((metrics.stringWidth(text) > path.getWidth()) &&
                        (text.indexOf(File.separatorChar, 4) > 0)) {
                    text = "..." + text.substring(text.indexOf(File.separatorChar, 4));
                }
            }
        }
        path.setText(text);
    }

    private static class SimpleListModel extends AbstractListModel {

        private final List<VirtualFile> list;

        public SimpleListModel(List<VirtualFile> list) {
            this.list = list;
        }

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return list.get(index);
        }
    }
}
