package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Timer;
import java.util.TimerTask;

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
	private final GameLogic logic;
	private final Sprite cockpit;
	private final HealthBar fuel, damage;

	private int width, height;

	private String message;
	private long fps;
	
	private boolean drawMessage;
	private boolean drawHUD;
	
	public GameOverlay(final GameLogic logic) {
		this.cockpit = SpriteCache.getInstance().getSprite("/res/cockpit.svg");
		this.logic = logic;
		logic.addListener(this);

		this.fuel = new HealthBar(SpriteCache.getInstance().getSprite("/res/fuel.svg"), "Fuel", Color.YELLOW,
				Color.YELLOW.darker(), 0, GameLogic.MAX_FUEL);

		this.damage = new HealthBar(SpriteCache.getInstance().getSprite("/res/asteroid.svg"), "Damage", Color.RED,
				Color.RED.darker(), 0, GameLogic.MAX_ASTEROIDS);

		this.timer = new Timer(GameOverlay.class.getSimpleName(), true);
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

		if (drawHUD) {
			fuel.setValue(logic.getFuel());
			damage.setValue(logic.getAsteroids());
	
			final int barsWidth = Math.min(width, height) / 2;
	
			// cockpit.render(g, 0, 0, width, height);
	
			damage.render(g, 20, 20, barsWidth, -1);
			fuel.render(g, 20, 50, barsWidth, -1);
		}
		
		g.setFont(SMALL);
		g.setColor(Color.WHITE);
		final FontMetrics fm = g.getFontMetrics();
		final int stringH = fm.getHeight();
		
		final String frames = fps + " FPS";
		final int framesW = fm.stringWidth(frames);
		g.drawString(frames, width - framesW - 20, stringH + 20);
		
		final String controls = "Pause/continue: SPACE";
		final int controlsW = fm.stringWidth(controls);
		
		g.drawString(controls, width - controlsW - 20, height - stringH);
	}

	@Override
	public void gameStarted(final GameLogic game) {
		drawHUD = true;
		message("Go speed racer!");
	}

	@Override
	public void gamePaused(final GameLogic game) {
		message("Paused. Zzzzz...", null);
	}
	
	@Override
	public void gameResumed(GameLogic game) {
		message("W00t! Continue!");
	}

	@Override
	public void gameStopped(final GameLogic game) {
		drawHUD = false;
		message("Click to play again!", null);
	}

	@Override
	public void collided(final GameLogic logic, final Item item) {
		if (item == Item.ASTEROID) {
			message("Oh noes! Evil asteroidz!");
		} else if (item == Item.FUELCAN) {
			message("Yay! Fuel FTW!");
		}
	}

	@Override
	public void updateFramesPerSecond(long fps) {
		this.fps = fps;
	}
	
	/**
	 * Displays a message on the screen for a limited time.
	 * 
	 * @param message
	 *            the message to display
	 * @param delay
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

	@Override
	public void componentHidden(final ComponentEvent e) {
		// empty
	}

	@Override
	public void componentMoved(final ComponentEvent e) {
		// empty
	}

	@Override
	public void componentResized(final ComponentEvent e) {
		this.width = e.getComponent().getWidth();
		this.height = e.getComponent().getHeight();
	}

	@Override
	public void componentShown(final ComponentEvent e) {
		// empty
	}
}
