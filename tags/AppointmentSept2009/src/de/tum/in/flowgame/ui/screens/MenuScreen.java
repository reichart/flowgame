package de.tum.in.flowgame.ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tum.in.flowgame.ui.GameMenu;

public abstract class MenuScreen extends JPanel {

	protected final GameMenu menu; // for subclasses

	public MenuScreen(final GameMenu menu) {
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
	public void update() {
		// for subclasses to override
	}

	/**
	 * Centers components both vertically and horizontically on the screen.
	 */
	public static Container centered(final JComponent... components) {
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

	public static JLabel title(final String title) {
		final JLabel label = new JLabel(title);
		label.setForeground(Color.WHITE);
		label.setFont(label.getFont().deriveFont(24f));
		return label;
	}
}