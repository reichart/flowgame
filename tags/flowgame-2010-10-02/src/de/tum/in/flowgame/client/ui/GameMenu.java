package de.tum.in.flowgame.client.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.client.GameApplet;
import de.tum.in.flowgame.client.engine.Game3D;
import de.tum.in.flowgame.client.ui.screens.AfterRoundQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.BeforeRoundQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.BeforeSessionQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.CreditsScreen;
import de.tum.in.flowgame.client.ui.screens.EmptyScreen;
import de.tum.in.flowgame.client.ui.screens.GameSessionScreen;
import de.tum.in.flowgame.client.ui.screens.IndividualHighscoresScreen;
import de.tum.in.flowgame.client.ui.screens.MainScreen;
import de.tum.in.flowgame.client.ui.screens.MenuScreen;
import de.tum.in.flowgame.client.ui.screens.PauseScreen;
import de.tum.in.flowgame.client.ui.screens.ProfileScreen;
import de.tum.in.flowgame.client.ui.screens.ProfileScreenIntro;
import de.tum.in.flowgame.client.ui.screens.SocialHighscoresScreen;
import de.tum.in.flowgame.client.ui.screens.SystemInfoScreen;
import de.tum.in.flowgame.client.ui.screens.story.AfterProfileScreen;
import de.tum.in.flowgame.client.ui.screens.story.BeforeProfileScreen;
import de.tum.in.flowgame.client.ui.screens.story.RoundExtroScreen;
import de.tum.in.flowgame.client.ui.screens.story.RoundIntroScreen;
import de.tum.in.flowgame.client.ui.screens.story.SessionExtroScreen;
import de.tum.in.flowgame.client.ui.screens.story.SessionIntroScreen;
import de.tum.in.flowgame.client.ui.sprite.Sprite;
import de.tum.in.flowgame.model.ScoringType;
import de.tum.in.flowgame.model.Collision.Item;

public class GameMenu implements Sprite, GameListener {

	private static final Log log = LogFactory.getLog(GameMenu.class);
	
	private GameLogic logic;

	private final OffscreenJPanel panel;

	private final CardLayout layout;

	private final JPanel screens;

	private final GameOverlay overlay;

	private final Game3D game;
	
	private Class<? extends MenuScreen> previous, current;

	private static SettingsPanel settings;
	
	public GameMenu(final Component mouseTrap, final GameOverlay overlay) {
		this.overlay = overlay;
		this.game = (Game3D) mouseTrap;

		this.layout = new CardLayout();
		this.screens = new JPanel(layout);
		screens.setDoubleBuffered(false); // hides white background
		screens.setOpaque(false);
		screens.setBounds(0, 0, GameApplet.WIDTH, GameApplet.HEIGHT);
		
		settings = SettingsPanel.getInstance(this); // singleton
		settings.setDoubleBuffered(false);
		settings.setBounds(0, 0, 200, 100);
		
		panel = new OffscreenJPanel(mouseTrap);
		panel.setLayout(null); // null layout for explicit positioning
		panel.setDoubleBuffered(false); // hides white background
		panel.setOpaque(false);
		
		panel.add(settings);
		panel.add(screens);
	}

	public void render(final Graphics2D g, final int x, final int y) {
		throw new UnsupportedOperationException();
	}

	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		final BufferedImage img = Utils.createImage(w, h);

		final Graphics2D offscreen = (Graphics2D) img.getGraphics();
		synchronized (panel) {
			panel.setSize(w, h);
			panel.paintAll(offscreen);
		}
		offscreen.dispose();

		g.drawImage(img, 0, 0, null);
	}

	public void added(final GameLogic game) {
		this.logic = game;

		// TODO the ugliest way to do dependency injection
		MenuScreen.menu = this;
		
		// these require the above logic to be available
		
		// various screens
		add(new EmptyScreen());
		add(new MainScreen());
		add(new SocialHighscoresScreen());
		add(new IndividualHighscoresScreen());
		add(new PauseScreen());
		add(new SystemInfoScreen());
		add(new CreditsScreen());
		add(new GameSessionScreen());
		
		// qn screens
		add(new BeforeSessionQuestionnaireScreen());
		add(new BeforeRoundQuestionnaireScreen());
		add(new AfterRoundQuestionnaireScreen());
		add(new ProfileScreen());
		
		// story screens
		add(new BeforeProfileScreen());
		add(new ProfileScreenIntro());
		add(new AfterProfileScreen());
		add(new SessionIntroScreen());
		add(new RoundIntroScreen());
		add(new RoundExtroScreen());
		add(new SessionExtroScreen());
		
		show(EmptyScreen.class);
		
		settings.loadSettings();
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

	public void gameResumed(final GameLogic game) {
		show(GameSessionScreen.class);
	}

	public void gameStarted(final GameLogic game) {
		show(GameSessionScreen.class);
	}

	public void gameStopped(final GameLogic game) {
		final ScoringType type = game.getCurrentGameSession().getType();
		log.info("game stopped, showing " + type + " highscore");
		switch (type) {
		case INDIVIDUAL:
			show(IndividualHighscoresScreen.class);
			break;
		case SOCIAL:
			show(SocialHighscoresScreen.class);
			break;
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
	private static void prepareForOffscreen(final Container root) {
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
		boolean found = false;
		for (final Component component : screens.getComponents()) {
			if (component.getClass().equals(screenClass)) {
				final MenuScreen screen = (MenuScreen) component;
				try {
					synchronized (panel) {
						screen.update(logic);
						prepareForOffscreen(screen);
					}
				} catch (final Exception ex) {
					log.error("failed to update screen for showing: " + screenClass.getName(), ex);
					return; // don't show when update failed
				}
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IllegalArgumentException("unknown screen: " + screenClass.getName());
		}
		
		synchronized (panel) {
			layout.show(screens, screenClass.getName());
			panel.revalidate();
		}
		this.previous = current;
		this.current = screenClass;
	}

	public Class<? extends MenuScreen> getPreviousScreen() {
		return previous;
	}
	
	public GameLogic getLogic() {
		return logic;
	}

	public GameOverlay getOverlay() {
		return overlay;
	}

	public Game3D getGame() {
		return this.game;
	}
}