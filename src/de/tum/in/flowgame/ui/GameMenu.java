package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import de.tum.in.flowgame.Game3D;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.ui.screens.EmptyScreen;
import de.tum.in.flowgame.ui.screens.GameOverScreen;
import de.tum.in.flowgame.ui.screens.HighscoresScreen;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.PauseScreen;
import de.tum.in.flowgame.ui.screens.QuestionnaireScreen;
import de.tum.in.flowgame.ui.screens.SettingsScreen;
import de.tum.in.flowgame.ui.screens.SystemInfoScreen;

public class GameMenu implements Sprite, GameListener {

	private GameLogic logic;

	private final OffscreenJPanel panel;

	private final CardLayout layout;

	private final JPanel screens;

	private final GameOverlay overlay;

	private final Game3D game;

	public GameMenu(final Component mouseTrap, final GameOverlay overlay) {
		this.overlay = overlay;
		this.game = (Game3D) mouseTrap;

		this.layout = new CardLayout();
		this.screens = new JPanel(layout);
		screens.setDoubleBuffered(false); // hides white background
		screens.setOpaque(false);

		panel = new OffscreenJPanel(mouseTrap);
		panel.setLayout(new BorderLayout());
		panel.setDoubleBuffered(false); // hides white background
		panel.setOpaque(false);
		panel.add(screens, BorderLayout.CENTER);
	}

	public void render(final Graphics2D g, final int x, final int y) {
		throw new UnsupportedOperationException();
	}

	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		final BufferedImage img = Utils.createImage(w, h);

		final Graphics2D offscreen = (Graphics2D) img.getGraphics();
		panel.setSize(w, h);
		panel.paintAll(offscreen);
		offscreen.dispose();

		g.drawImage(img, 0, 0, null);
	}

	public void added(final GameLogic game) {
		this.logic = game;

		// these require the above logic to be available
		add(new EmptyScreen(this));
		add(new MainScreen(this));
		add(new QuestionnaireScreen(this));
		add(new HighscoresScreen(this));
		add(new PauseScreen(this));
		add(new GameOverScreen(this));
		add(new SettingsScreen(this));
		add(new SystemInfoScreen(this));

		show(MainScreen.class);
	}

	public void removed(final GameLogic game) {
		// empty
	}

	public void collided(final GameLogic logic, final Item item) {
		// empty
	}

	public void gamePaused(final GameLogic game) {
		show(PauseScreen.class);
	}

	// @Override
	// public void sessionFinished(GameLogic game) {
	// show(GameOverScreen.class);
	// }

	public void gameResumed(final GameLogic game) {
		// empty
	}

	public void gameStarted(final GameLogic game) {
		show(EmptyScreen.class);
	}

	public void gameStopped(final GameLogic game) {
		if (game.getCurrentScenarioRound().getQuestionnaire() != null) {
			show(QuestionnaireScreen.class);
		}
	}

	private void add(final MenuScreen screen) {
		screen.build();
		prepareForOffscreen(screen);
		screens.add(screen.getClass().getName(), screen);
	}

	/**
	 * Fixes various problem with rendering components offscreen outside of the
	 * usual Swing/AWT magic.
	 */
	private void prepareForOffscreen(final Container root) {
		for (final Component comp : root.getComponents()) {

			// white text color for everything
			comp.setForeground(Color.WHITE);

			if (comp instanceof JComponent) {
				final JComponent jcomp = (JComponent) comp;
				jcomp.setDoubleBuffered(false);
				jcomp.setOpaque(false);

				// prevent NPE when requesting focus on JRE 1.5
				jcomp.setRequestFocusEnabled(false);
			}

			if (comp instanceof JButton) {
				comp.setForeground(Color.DARK_GRAY);
			}

			if (comp instanceof JTable) {
				comp.setForeground(Color.WHITE);
				comp.setBackground(new Color(1f, 1f, 1f, 0f));
				comp.setFont(comp.getFont().deriveFont(18f));
			}

			if (comp instanceof JTableHeader) {
				comp.setForeground(Color.DARK_GRAY);
				comp.setFont(comp.getFont().deriveFont(Font.BOLD, 20f));
			}

			if (comp instanceof JScrollPane) {
				// transparent white bg is easier to read
				comp.setBackground(new Color(1f, 1f, 1f, .25f));
				((JComponent) comp).setOpaque(true);
			}

			if (comp instanceof Container) {
				prepareForOffscreen((Container) comp);
			}

		}
	}

	public void show(final Class<? extends MenuScreen> screenClass) {
		for (final Component component : screens.getComponents()) {
			if (component.getClass().equals(screenClass)) {
				final MenuScreen screen = (MenuScreen) component;
				screen.update();
				prepareForOffscreen(screen);
			}
		}

		layout.show(screens, screenClass.getName());
		panel.revalidate();
	}

	public GameLogic getGameLogic() {
		return logic;
	}

	public GameLogic getLogic() {
		return getGameLogic();
	}

	public GameOverlay getOverlay() {
		return overlay;
	}

	public Game3D getGame() {
		return this.game;
	}
}