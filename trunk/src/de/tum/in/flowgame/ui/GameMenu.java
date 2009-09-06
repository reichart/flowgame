package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.ui.screens.GameOverScreen;
import de.tum.in.flowgame.ui.screens.HighscoresScreen;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.PauseScreen;

public class GameMenu implements Sprite, GameListener {

	private final GameLogic logic;

	private final OffscreenJPanel panel;

	private final CardLayout layout;

	private final JPanel screens;

	public GameMenu(final Component mouseTrap, final GameLogic logic) {
		this.logic = logic;

		this.layout = new CardLayout();
		this.screens = new JPanel(layout);
		screens.setDoubleBuffered(false); // hides white background
		screens.setOpaque(false);

		add(new MainScreen(this));
		add(new HighscoresScreen(this));
		add(new PauseScreen(this));
		add(new GameOverScreen(this));

		panel = new OffscreenJPanel(mouseTrap);
		panel.setLayout(new BorderLayout());
		panel.setDoubleBuffered(false); // hides white background
		panel.setOpaque(false);
		panel.add(screens, BorderLayout.CENTER);
		
		logic.addListener(this);
	}

	@Override
	public void render(final Graphics2D g, final int x, final int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		final BufferedImage img = CachedSprite.createImage(w, h);

		final Graphics2D offscreen = (Graphics2D) img.getGraphics();
		panel.setSize(w, h);
		panel.paintAll(offscreen);
		offscreen.dispose();

		g.drawImage(img, 0, 0, null);
	}

	@Override
	public void collided(final GameLogic logic, final Item item) {
		// empty
	}

	@Override
	public void gamePaused(final GameLogic game) {
		System.out.println("GameMenu.gamePaused()");
		show(PauseScreen.class);
	}

	@Override
	public void gameResumed(final GameLogic game) {
		System.out.println("GameMenu.gameResumed()");
		// empty
	}

	@Override
	public void gameStarted(final GameLogic game) {
		System.out.println("GameMenu.gameStarted()");
		// empty
	}

	@Override
	public void gameStopped(final GameLogic game) {
		System.out.println("GameMenu.gameStopped()");
		show(GameOverScreen.class);
	}

	private void add(final MenuScreen screen) {
		final Container contents = screen.getContents();
		setTransparent(contents);
		screens.add(screen.getClass().getName(), contents);
	}

	/**
	 * Calls {@link JComponent#setOpaque(boolean)} with <code>false</code> on
	 * all {@link JComponent}s in the tree.
	 */
	private void setTransparent(final Container root) {
		for (final Component comp : root.getComponents()) {
			if (comp instanceof JComponent) {
				final JComponent jcomp = (JComponent) comp;
				jcomp.setOpaque(false);
			}

			if (comp instanceof Container) {
				setTransparent((Container) comp);
			}
		}
	}

	public void show(final Class<? extends MenuScreen> screen) {
		System.out.println("GameMenu.show() " + screen);
		layout.show(screens, screen.getName());
		panel.revalidate();
	}

	public GameLogic getLogic() {
		return logic;
	}
}
