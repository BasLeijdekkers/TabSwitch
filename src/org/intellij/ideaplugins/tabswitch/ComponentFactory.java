package org.intellij.ideaplugins.tabswitch;

import javax.swing.JComponent;

/**
 * Creates an instance of {@link JComponent}.
 * <pre>
 * User: must
 * Date: 2012-05-30
 * </pre>
 */
public interface ComponentFactory<C extends JComponent> {

  C create();
}
