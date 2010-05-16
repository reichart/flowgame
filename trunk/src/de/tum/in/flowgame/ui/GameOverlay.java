package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.vecmath.Point2d;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.engine.Game3D;
import de.tum.in.flowgame.engine.behavior.FrameCounterBehavior.FrameCounterListener;
import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.ui.sprite.HealthBar;
import de.tum.in.flowgame.ui.sprite.SpriteCache;

/**
 * Displays overlay graphics like health/damage bars, cockpit, and HUD messages.
 */
public class GameOverlay implements GameListener, ComponentListener, FrameCounterListener {

	private final static Font LARGE = new Font("sans", Font.BOLD, 48);
	private final static Font MEDIUM = new Font("sans", Font.BOLD, 30);
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
	
	private List<ScreenMessage> messages;
	
	private final Game3D engine;

	public GameOverlay(final Game3D engine) {
		this.menu = new GameMenu(engine, this);
		
		this.fuel = new HealthBar(SpriteCache.getInstance().getSprite("/res/fuel.svg"), Color.YELLOW,
				Color.YELLOW.darker(), 0, GameLogic.MAX_FUEL);

		this.damage = new HealthBar(SpriteCache.getInstance().getSprite("/res/asteroid.svg"), Color.RED,
				Color.RED.darker(), 0, GameLogic.MAX_ASTEROIDS);

		this.timer = new Timer(GameOverlay.class.getSimpleName(), true);

		this.drawMenu = true; // for testing
		
		this.engine = engine;
		
		this.messages = new CopyOnWriteArrayList<ScreenMessage>();
	}

	public GameMenu getMenu() {
		return menu;
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
		
		g.setFont(MEDIUM);
		for (ScreenMessage message : messages) {
			g.setColor(message.getColor());
			g.drawString(message.getMessage(), message.getX(), message.getY());
		}

		g.setFont(SMALL);
		g.setColor(Color.WHITE);
		final FontMetrics fm = g.getFontMetrics();
		final int stringH = fm.getHeight();

		if (drawFPS) {
			final String frames = fps + " FPS";
			final int framesW = fm.stringWidth(frames);
			g.drawString(frames, width - framesW - 20, stringH);
		}

		if (drawHUD) {
			final String controls = "Pause/continue: SPACE";
			final int controlsW = fm.stringWidth(controls);
			g.drawString(controls, width - controlsW - 20, height - stringH);
			
			if (logic != null) {
				String score = "Score " + logic.getScore();
				final int scoreW = fm.stringWidth(score);
				g.drawString(score, width - scoreW - 20, stringH + 20);
				
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
		Point2d coords = engine.getShip2DCoords();
		if (item == Item.ASTEROID) {
			message(String.valueOf(logic.getPointsAdded()), Color.RED, coords);
			//message("Oh noes! Evil asteroidz!");
		} else if (item == Item.FUELCAN) {
			message(String.valueOf(logic.getPointsAdded()), Color.YELLOW, coords);
			//message("Yay! Fuel FTW!");
		}
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
	
	public void message(final String message, final Color color, final Point2d position) {
		ScreenMessage m = new ScreenMessage(message, color, position);
		messages.add(m);
		timer.schedule(new ScoreAnimationTask(m), 0, 20);
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
	
	private class ScoreAnimationTask extends TimerTask {
		private ScreenMessage message;
		private int counter;
		
		public ScoreAnimationTask(ScreenMessage message) {
			this.message = message;
			this.counter = 0;
		}

		@Override
		public void run() {
			if (counter < 20) {
				message.move(0, -2);
				counter++;
			} else {
				messages.remove(message);
				this.cancel();
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
