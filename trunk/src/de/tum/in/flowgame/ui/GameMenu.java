package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.tum.in.flowgame.Game3D;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.ui.screens.GameOverScreen;
import de.tum.in.flowgame.ui.screens.HighscoresScreen;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.PauseScreen;
import de.tum.in.flowgame.ui.screens.QuestionnaireScreen;
import de.tum.in.flowgame.ui.screens.SettingsScreen;

public class GameMenu implements Sprite, GameListener {

	private final GameLogic logic;

	private final OffscreenJPanel panel;

	private final CardLayout layout;

	private final JPanel screens;

	private final GameOverlay overlay;
	
	private final Game3D game;

	public GameMenu(final Component mouseTrap, final GameLogic logic, final GameOverlay overlay) {
		this.logic = logic;
		this.overlay = overlay;
		this.game = (Game3D)mouseTrap;

		this.layout = new CardLayout();
		this.screens = new JPanel(layout);
		screens.setDoubleBuffered(false); // hides white background
		screens.setOpaque(false);

		add(new MainScreen(this));
		add(new QuestionnaireScreen(this));
		add(new HighscoresScreen(this));
		add(new PauseScreen(this));
		add(new GameOverScreen(this));
		add(new SettingsScreen(this));

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
		show(PauseScreen.class);
	}

	@Override
	public void gameResumed(final GameLogic game) {
		// empty
	}

	@Override
	public void gameStarted(final GameLogic game) {
		// empty
	}

	@Override
	public void gameStopped(final GameLogic game) {
		show(GameOverScreen.class);
	}

	private void add(final MenuScreen screen) {
		screen.build();
		setTransparent(screen);
		screens.add(screen.getClass().getName(), screen);
	}

	/**
	 * Calls {@link JComponent#setOpaque(boolean)} with <code>false</code> on
	 * all {@link JComponent}s in the tree.
	 */
	private void setTransparent(final Container root) {
		for (final Component comp : root.getComponents()) {
			if (comp instanceof JComponent) {
				final JComponent jcomp = (JComponent) comp;
				jcomp.setDoubleBuffered(false);
				jcomp.setOpaque(false);
			}

			if (comp instanceof Container) {
				setTransparent((Container) comp);
			}
		}
	}

	public void show(final Class<? extends MenuScreen> screenClass) {
		for (final Component component : screens.getComponents()) {
			if (component.getClass().equals(screenClass)) {
				final MenuScreen screen = (MenuScreen) component;
				screen.update();
			}
		}

		layout.show(screens, screenClass.getName());
		panel.revalidate();
	}

	public GameLogic getLogic() {
		return logic;
	}
	
	public GameOverlay getOverlay() {
		return overlay;
	}
	
	public Game3D getGame (){
		return this.game;
	}
}
