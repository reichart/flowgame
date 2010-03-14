package de.tum.in.flowgame.ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.ui.GameMenu;

public abstract class MenuScreen extends JPanel {

	protected final GameMenu menu; // for subclasses

	protected MenuScreen(final GameMenu menu) {
		setLayout(new BorderLayout());
		setDoubleBuffered(false);
		setOpaque(false);

		this.menu = menu;
	}

	public void build() {
		add(BorderLayout.CENTER, getContents());
	}

	public abstract Container getContents();

	/**
	 * Called before every time this screen will be shown.
	 */
	public void update(GameLogic logic) throws Exception {
		// for subclasses to override
	}

	/**
	 * Adds a titled border to a component.
	 */
	protected static <C extends JComponent> C titled(final C comp, final String title) {
		final TitledBorder border = BorderFactory.createTitledBorder(title);
		border.setTitleColor(Color.WHITE);
		comp.setBorder(border);
		return comp;
	}

	/**
	 * Centers components both vertically and horizontically on the screen.
	 */
	protected static Container centered(final JComponent... components) {
		final Box column = new Box(BoxLayout.Y_AXIS);
		column.setOpaque(false);
		column.add(Box.createVerticalGlue());
		for (final JComponent component : components) {
			component.setAlignmentX(Component.CENTER_ALIGNMENT);
			column.add(component);
			column.add(Box.createVerticalStrut(20));
		}
		column.add(Box.createVerticalGlue());

		final Box center = new Box(BoxLayout.X_AXIS);
		center.setOpaque(false);
		center.add(Box.createHorizontalGlue());
		center.add(column);
		center.add(Box.createHorizontalGlue());

		return center;
	}

	protected static JLabel title(final String title) {
		final JLabel label = new JLabel(title);
		label.setForeground(Color.WHITE);
		label.setFont(label.getFont().deriveFont(24f));
		return label;
	}

	protected JButton goTo(final String label, final Class<? extends MenuScreen> screen) {
		return new JButton(new AbstractAction(label) {
			public void actionPerformed(final ActionEvent e) {
				menu.show(screen);
			}
		});
	}
}