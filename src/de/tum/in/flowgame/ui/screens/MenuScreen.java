package de.tum.in.flowgame.ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.ui.GameMenu;

/**
 * Abstract base class for all screens in the game.
 */
public abstract class MenuScreen extends JPanel {

	public static final int BORDER_WIDTH = 20;
	public static final int BORDER_WIDTH_TOP = 10;
	private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(BORDER_WIDTH_TOP, 0, 0, 0);
	
	public static GameMenu menu; // for subclasses
	private final BufferedImage backgroundImage;
	

	private static Border border;
	
	/**
	 * @param menu the game menu managing all screens
	 * @param backgroundImage the optional background image, <code>null</code> to disable
	 */
	protected MenuScreen(final BufferedImage backgroundImage, Border cborder) {
		setLayout(new BorderLayout());
		setDoubleBuffered(false);
		setOpaque(false);

		this.backgroundImage = backgroundImage;
		border = cborder;
	}
	
	protected MenuScreen(final BufferedImage backgroundImage) {
		this(backgroundImage, DEFAULT_BORDER);
	}
	
	protected MenuScreen(Border border) {
		this(null, border);
	}
	
	protected MenuScreen() {
		this(null, DEFAULT_BORDER);
	}

	public void build() {
		add(BorderLayout.CENTER, getContents());
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}
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
		center.setBorder(border);

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