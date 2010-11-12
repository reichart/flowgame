package de.tum.in.flowgame.client.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
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
import de.tum.in.flowgame.client.engine.Game3D;
import de.tum.in.flowgame.client.engine.behavior.FrameCounterBehavior.FrameCounterListener;
import de.tum.in.flowgame.client.ui.screens.UIMessages;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.Collision.Item;

/**
 * Displays overlay graphics like health/damage bars, cockpit, and HUD messages.
 */
public class GameOverlay implements GameListener, ComponentListener, FrameCounterListener {

	private final static Font XLARGE = new Font("sans", Font.BOLD, 48);
	private final static Font LARGE = new Font("sans", Font.BOLD, 30);
	private final static Font MEDIUM = new Font("sans", Font.PLAIN, 20);
	private final static Font MEDIUM_BOLD = new Font("sans", Font.BOLD, 20);
	private final static Font SMALL = new Font("sans", Font.PLAIN, 16);

	// radius of circle surrounding timer animation
	private final static int RADIUS = 20;
	private final static int UPPER_BORDER = 5;
	private final static int CIRCLE_BORDER = 5;

	private final Timer timer;
	private volatile GameLogic logic;

	private int width, height;

	private String message;
	private long fps;

	private boolean drawMessage;
	private boolean drawHUD;
	private boolean drawFPS;

	private GameMenu menu;

	private List<ScreenMessage> messages;

	private final Game3D engine;

	private TimeLimitAnitmationTask timerAnimation;

	public GameOverlay(final Game3D engine) {
		this.menu = new GameMenu(engine, this);

		this.timer = new Timer(GameOverlay.class.getSimpleName(), true);

		this.engine = engine;

		this.messages = new CopyOnWriteArrayList<ScreenMessage>();
	}

	public GameMenu getMenu() {
		return menu;
	}

	public void render(final Graphics2D g) {
		final FontMetrics fmXLarge = g.getFontMetrics(XLARGE);
		final FontMetrics fmMedium = g.getFontMetrics(MEDIUM);
		final FontMetrics fmMediumBold = g.getFontMetrics(MEDIUM_BOLD);
		final FontMetrics fmSmall = g.getFontMetrics(SMALL);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (drawMessage) {
			g.setFont(XLARGE);
			g.setColor(Color.WHITE);

			final int w = fmXLarge.stringWidth(message);
			final int h = fmXLarge.getHeight();

			g.drawString(message, (width - w) / 2, (height - h) / 2);
		}

		g.setFont(LARGE);
		for (ScreenMessage message : messages) {
			g.setColor(message.getColor());
			g.drawString(message.getMessage(), message.getX(), message.getY());
		}

		g.setFont(SMALL);
		g.setColor(Color.WHITE);
		final int stringH = fmSmall.getHeight();

		if (drawFPS) {
			final String frames = fps + " " + UIMessages.getString("fps");
			final int framesW = fmSmall.stringWidth(frames);
			g.drawString(frames, width - framesW - 20, stringH);
		}

		if (drawHUD) {
			final String controls = UIMessages.getString("pause.continue");
			final int controlsW = fmSmall.stringWidth(controls);
			g.drawString(controls, width - controlsW - 20, height - stringH);
			
			if (logic != null) {
				// render Score in the upper middle of screen
				g.setFont(MEDIUM);
				String scoreValue = "" + logic.getScore();
				g.drawString(scoreValue, width / 2, fmMedium.getAscent() + UPPER_BORDER);
				g.setFont(MEDIUM_BOLD);
				g.drawString(UIMessages.getString("score") + " ", width / 2 - fmMediumBold.stringWidth(UIMessages.getString("score") + " "), fmMediumBold.getAscent()
						+ UPPER_BORDER);

				final ScenarioRound round = logic.getCurrentScenarioRound();
				
				// get remaining time in seconds
				String remainingTime = String.valueOf(logic.getRemainingTime() / 1000);
				// if remainingTime changed (one second is over) start new
				// animation
				if (timerAnimation == null || !timerAnimation.getMessage().getMessage().equals(remainingTime)) {
					Color color;
					// change colors from green to yellow to red the fewer time
					// is left
					if (logic.getRemainingTime() > (round.getExpectedPlaytime() / 2)) {
						color = Color.GREEN;
					} else if (logic.getRemainingTime() > (round.getExpectedPlaytime() / 4)) {
						color = Color.YELLOW;
					} else {
						color = Color.RED;
					}
					timerAnimation = new TimeLimitAnitmationTask(new ScreenMessage(remainingTime, color, new Point2d(
							width - RADIUS * 2, RADIUS + UPPER_BORDER + CIRCLE_BORDER), MEDIUM.getSize()));
					timer.schedule(timerAnimation, 0, 40);
				}

				// draw timer in upper right corner with parameters from
				// timerAnimation
				g.setFont(new Font("sans", Font.BOLD, timerAnimation.getMessage().getSize()));
				final FontMetrics fmTimer = g.getFontMetrics();
				final int remTimeWidth = fmTimer.stringWidth(remainingTime);
				final int remTimeHeight = fmTimer.getAscent();
				g.setColor(timerAnimation.getMessage().getColor());
				int xPos = timerAnimation.getMessage().getX();
				int yPos = timerAnimation.getMessage().getY();
				g.drawString(remainingTime, xPos - remTimeWidth / 2, yPos - 3 + remTimeHeight / 2);

				// draw surrounding circle
				g.setStroke(new BasicStroke(CIRCLE_BORDER));
				Paint paint = new GradientPaint(xPos - 20, yPos - RADIUS, timerAnimation.getMessage().getColor(), xPos
						+ RADIUS, yPos + RADIUS, timerAnimation.getMessage().getColor().darker().darker());
				g.setPaint(paint);
				
				// display circle that shrinks with the time
				double playtimeOver = (double) logic.getRemainingTime() / round.getExpectedPlaytime();
				for (int i = 0; i < 12 * playtimeOver; i++) {
					g.drawArc(xPos - RADIUS, yPos - RADIUS, RADIUS * 2, RADIUS * 2, (i*30 + 90) % 360, 10);
				}
			}
		}

		menu.render(g, 0, 0, width, height);
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
		message(UIMessages.getString("game.go"));
	}

	public void gamePaused(final GameLogic game) {
		drawMessage = false;
	}

	public void gameResumed(GameLogic game) {
		// empty
	}

	public void gameStopped(final GameLogic game) {
		drawHUD = false;
		drawMessage = false;
	}

	public void collided(final GameLogic logic, final Item item) {
		Point2d coords = engine.getShip2DCoords();
		if (item == Item.ASTEROID) {
			message(String.valueOf(logic.getPointsAdded()), Color.RED, coords);
			// message("Oh noes! Evil asteroidz!");
		} else if (item == Item.FUELCAN) {
			message(String.valueOf(logic.getPointsAdded()), Color.YELLOW, coords);
			// message("Yay! Fuel FTW!");
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

	private class TimeLimitAnitmationTask extends TimerTask {
		private ScreenMessage message;
		private int counter;

		public TimeLimitAnitmationTask(ScreenMessage message) {
			this.message = message;
			this.counter = 0;
		}

		@Override
		public void run() {
			if (counter < 5) {
				message.setSize(message.getSize() + 1);
				counter++;
			} else if (counter < 18) {
				message.setSize(message.getSize() - 1);
				counter++;
			} else {
				messages.remove(message);
				this.cancel();
			}
		}

		public ScreenMessage getMessage() {
			return message;
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

	// @Override
	// public void sessionFinished(GameLogic game) {
	// // empty
	// }
}
