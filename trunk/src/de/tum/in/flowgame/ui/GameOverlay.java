package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Timer;
import java.util.TimerTask;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogic.Item;

/**
 * Displays overlay graphics like health/damage bars, cockpit, and HUD messages.
 */
public class GameOverlay implements GameListener, ComponentListener {

	private final Timer timer;
	private final GameLogic logic;
	private final Font bigFont;
	private final Sprite cockpit;
	private final HealthBar fuel, damage;

	private int width, height;

	private String message;

	public GameOverlay(final GameLogic logic) {
		this.cockpit = SpriteCache.getInstance().getSprite("/res/cockpit.svg");
		this.bigFont = new Font("sans", Font.BOLD, 24);
		this.logic = logic;

		this.fuel = new HealthBar(SpriteCache.getInstance().getSprite("/res/fuel.svg"), "Fuel", Color.YELLOW,
				Color.YELLOW.darker(), 0, GameLogic.MAX_FUEL);

		this.damage = new HealthBar(SpriteCache.getInstance().getSprite("/res/asteroid.svg"), "Damage", Color.RED,
				Color.RED.darker(), 0, GameLogic.MAX_ASTEROIDS);

		this.timer = new Timer(GameOverlay.class.getSimpleName(), true);
	}

	public void render(final Graphics2D g) {
		g.setFont(bigFont);

		if (message != null) {
			final FontMetrics fm = g.getFontMetrics();
			final int w = fm.stringWidth(message);
			final int h = fm.getHeight();

			g.drawString(message, (width + w) / 2, h);
		}

		fuel.setValue(logic.getFuel());
		damage.setValue(logic.getAsteroids());

		final int barsWidth = Math.min(width, height) / 2;

//		cockpit.render(g, 0, 0, width, height);

		damage.render(g, 20, 20, barsWidth, -1);
		fuel.render(g, 20, 50, barsWidth, -1);
	}

	@Override
	public void gameStarted(final GameLogic game) {
		message("Go speed racer!");
	}

	@Override
	public void gamePaused(final GameLogic game) {
		message("Paused. Zzzzz...");
	}

	@Override
	public void gameStopped(final GameLogic game) {
		message("Iz ovurr!");
	}

	@Override
	public void collided(final GameLogic logic, final Item item) {
		message("Oh noes! Collishun with " + item.toString().toLowerCase() + "!");
	}

	/**
	 * Displays a message on the screen for a limited time.
	 * 
	 * @param message
	 *            the message to display
	 * @param delay
	 *            the time to display the message in milliseconds
	 */
	public void message(final String message, final long delay) {
		this.message = message;
		timer.schedule(new ClearMessage(), delay);
	}

	/**
	 * Displays a message on the screen for two seconds.
	 */
	public void message(final String message) {
		this.message = message;
		timer.schedule(new ClearMessage(), 2000);
	}

	private class ClearMessage extends TimerTask {
		@Override
		public void run() {
			message = null;
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
