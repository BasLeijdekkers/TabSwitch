package org.intellij.ideaplugins.tabswitch;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.ui.UIUtil;
import com.intellij.peer.PeerFactory;
import com.intellij.ui.UIHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.Icon;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

final class TabSwitchListCellRenderer extends JLabel implements ListCellRenderer {

	private static final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
	private static final Border SELECTION_BORDER = new SelectionBorder();
	private PsiManager manager;
	private FileStatusManager fileStatusManager;


	TabSwitchListCellRenderer(Project project) {
		manager = PsiManager.getInstance(project);
		fileStatusManager = FileStatusManager.getInstance(project);
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList jList, Object value, int index,
	                                              boolean isSelected, boolean cellHasFocus) {
		final VirtualFile virtualFile = (VirtualFile)value;
		setText(virtualFile.getName());
		final Icon icon = getIcon(virtualFile);
		setIcon(icon);

		final Color statusColor = getStatusColor(virtualFile);
		setForeground(statusColor);
		final Border border;
		final Color background;
		if (isSelected) {
			border = SELECTION_BORDER;
			background = UIUtil.getListSelectionBackground();
		} else {
			border = EMPTY_BORDER;
			background = null;
		}
		setBorder(border);
		setBackground(background);
		return this;
	}

	private Color getStatusColor(VirtualFile virtualFile) {
		final FileStatus status = fileStatusManager.getStatus(virtualFile);
		return status.getColor();
	}

	private Icon getIcon(VirtualFile virtualFile) {
		final PsiFile file = manager.findFile(virtualFile);
		final Icon icon;
		if (file == null) {
			icon = virtualFile.getIcon();
		} else {
			icon = file.getIcon(Iconable.ICON_FLAG_READ_STATUS);
		}
		return icon;
	}

	private static final class SelectionBorder implements Border {

		private final Insets insets;

		SelectionBorder() {
			insets = new Insets(1, 1, 1, 1);
		}

		public void paintBorder(Component component, Graphics g, int x, int y, int width,
		                        int height) {
			g.setColor(Color.BLACK);
			final PeerFactory peerFactory = PeerFactory.getInstance();
			final UIHelper uiHelper = peerFactory.getUIHelper();
			uiHelper.drawDottedRectangle(g, x, y, x + width - 1, y + height - 1);
		}

		public Insets getBorderInsets(Component component) {
			return insets;
		}

		public boolean isBorderOpaque() {
			return true;
		}
	}
}