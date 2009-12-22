package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import de.tum.in.flowgame.Game3D;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.FrameCounterBehavior.FrameCounterListener;
import de.tum.in.flowgame.model.Collision.Item;

/**
 * Displays overlay graphics like health/damage bars, cockpit, and HUD messages.
 */
public class GameOverlay implements GameListener, ComponentListener, FrameCounterListener {

	private final static Font LARGE = new Font("sans", Font.BOLD, 48);
	private final static Font SMALL = new Font("sans", Font.PLAIN, 16);

	private final Timer timer;
	private volatile GameLogic logic;
	private final HealthBar fuel, damage;

	private int width, height;

	private String message;
	private long fps;

	private boolean drawMessage;
	private boolean drawHUD;
	private boolean drawMenu;
	private boolean drawFPS;

	private GameMenu menu;

	public GameOverlay(final Game3D engine) {
		this.menu = new GameMenu(engine, this);
		
		this.fuel = new HealthBar(SpriteCache.getInstance().getSprite("/res/fuel.svg"), "Fuel", Color.YELLOW,
				Color.YELLOW.darker(), 0, GameLogic.MAX_FUEL);

		this.damage = new HealthBar(SpriteCache.getInstance().getSprite("/res/asteroid.svg"), "Damage", Color.RED,
				Color.RED.darker(), 0, GameLogic.MAX_ASTEROIDS);

		this.timer = new Timer(GameOverlay.class.getSimpleName(), true);

		this.drawMenu = true; // for testing
	}

	public void render(final Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (drawMessage) {
			g.setFont(LARGE);
			g.setColor(Color.WHITE);

			final FontMetrics fm = g.getFontMetrics();
			final int w = fm.stringWidth(message);
			final int h = fm.getHeight();

			g.drawString(message, (width - w) / 2, (height - h) / 2);
		}

		g.setFont(SMALL);
		g.setColor(Color.WHITE);
		final FontMetrics fm = g.getFontMetrics();
		final int stringH = fm.getHeight();

		if (drawFPS) {
			final String frames = fps + " FPS";
			final int framesW = fm.stringWidth(frames);
			g.drawString(frames, width - framesW - 20, stringH + 20);
		}

		if (drawHUD) {
			final String controls = "Pause/continue: SPACE";
			final int controlsW = fm.stringWidth(controls);
			g.drawString(controls, width - controlsW - 20, height - stringH);
			
			if (logic != null) {
				final NumberFormat fmt = new DecimalFormat("0.000");
				
				final String ratios = "Total: " + fmt.format(logic.getTotalFuelRatio()) + " fuel, " + fmt.format(logic.getTotalAsteroidRatio()) + " astr.";
				final int ratiosW = fm.stringWidth(ratios);
				g.drawString(ratios, width - ratiosW - 20, stringH + 50);
				
				final String slidingRatios = "SlidingWindow: " + fmt.format(logic.getSlidingFuelRatio()) + " fuel, " + fmt.format(logic.getSlidingAsteroidRatio()) + " astr.";
				final int slidingRatiosW = fm.stringWidth(slidingRatios);
				g.drawString(slidingRatios, width - slidingRatiosW -20, stringH + 70);
	
				String score = "Score " + logic.getScore();
				final int scoreW = fm.stringWidth(score);
				g.drawString(score, width - scoreW - 20, stringH + 120);
				
				String rating = "Rating " + fmt.format(logic.getRating());
				final int ratingW = fm.stringWidth(rating);
				g.drawString(rating, width - ratingW - 20, stringH + 170);
				
				fuel.setValue(logic.getFuel());
				damage.setValue(logic.getAsteroids());
	
				final int barsWidth = Math.min(width, height) / 2;
	
				damage.render(g, 20, 20, barsWidth, -1);
				fuel.render(g, 20, 50, barsWidth, -1);
			}
		}

		if (drawMenu) {
			menu.render(g, 0, 0, width, height);
		}
	}

	public void added(final GameLogic game) {
		this.logic = game;
		game.addListener(menu);
	}
	
	public void removed(final GameLogic game) {
		this.logic = null;
		game.removeListener(menu);
	}
	
	public void gameStarted(final GameLogic game) {
		drawHUD = true;
		drawMenu = false;
		message("Go speed racer!");
	}

	public void gamePaused(final GameLogic game) {
		drawMenu = true;
		drawMessage = false;
	}

	public void gameResumed(GameLogic game) {
		drawMenu = false;
	}

	public void gameStopped(final GameLogic game) {
		drawHUD = false;
		drawMenu = true;
		drawMessage = false;
	}

	public void collided(final GameLogic logic, final Item item) {
//		if (item == Item.ASTEROID) {
//			message("Oh noes! Evil asteroidz!");
//		} else if (item == Item.FUELCAN) {
//			message("Yay! Fuel FTW!");
//		}
	}

	public void updateFramesPerSecond(long fps) {
		this.fps = fps;
	}

	/**
	 * Displays a message on the screen for a limited time.
	 * 
	 * @param message
	 *            the message to display
	 * @param seconds
	 *            the time to display the message in milliseconds
	 */
	public void message(final String message, final Integer seconds) {
		this.message = message;
		this.drawMessage = true;

		if (seconds != null) {
			timer.schedule(new MessageTimer(message), seconds * 1000);
		}
	}

	/**
	 * Displays a message on the screen for two seconds.
	 */
	public void message(final String message) {
		message(message, 2);
	}

	private class MessageTimer extends TimerTask {

		private final String messageToClear;

		public MessageTimer(final String messageToClear) {
			this.messageToClear = messageToClear;
		}

		@Override
		public void run() {
			if (messageToClear.equals(message)) {
				drawMessage = false;
			}
		}
	}

	public void componentHidden(final ComponentEvent e) {
		// empty
	}

	public void componentMoved(final ComponentEvent e) {
		// empty
	}

	public void componentResized(final ComponentEvent e) {
		this.width = e.getComponent().getWidth();
		this.height = e.getComponent().getHeight();
	}

	public void componentShown(final ComponentEvent e) {
		// empty
	}
	
	public void setDrawFPS(final boolean drawFPS) {
		this.drawFPS = drawFPS;
	}

//	@Override
//	public void sessionFinished(GameLogic game) {
//		// empty		
//	}
}
