package org.intellij.ideaplugins.tabswitch;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

/**
 * Creates a {@link ListSelectionListener}. Updates the path to currently selected file in the popup window.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public interface ListSelectionListenerFactory<S extends ListSelectionListener> {

  S create(JList list, JLabel pathLabel);
}
