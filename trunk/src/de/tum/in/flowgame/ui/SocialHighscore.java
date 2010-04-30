package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import netscape.javascript.JSObject;
import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.facebook.FacebookFriendCache;
import de.tum.in.flowgame.model.Highscore;

public class SocialHighscore extends JPanel {

	private static final boolean CURVE = true;

	private final FacebookFriendCache friendCash;
	private final FriendsBar fb;
	private Highscore ownScore;
	private List<Highscore> highscores;
	private final Client client;
	private CustomButton personButton;

	public SocialHighscore(final Client client, final FacebookFriendCache friendCash, JSObject win) {
		this.client = client;
		this.friendCash = friendCash;

		try {
			update();

			this.setLayout(new BorderLayout());
			fb = new FriendsBar(highscores, friendCash, win);
			this.add(fb, BorderLayout.SOUTH);
		} catch (Exception ex) {
			// TODO handle exception
			throw new RuntimeException(ex);
		}

		personButton = new CustomButton();
	}

	public void update() throws Exception {
		friendCash.updateFriends();
		updateHighscores();
	}

	private void updateHighscores() throws Exception {
		final List<Long> persons = friendCash.getFriendsids();
		persons.add(friendCash.getCurrentPlayer().getId());

		final List<Highscore> globalScores = client.getHighscores(persons);

		final long currentID = friendCash.getCurrentPlayer().getId();
		for (final Iterator<Highscore> iterator = globalScores.iterator(); iterator.hasNext();) {
			final Highscore highscore = iterator.next();
			if (highscore.getPersonid() == currentID) {
				ownScore = highscore;
				iterator.remove();
			}
		}
		// globalScores.remove(ownScore);
		highscores = globalScores;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		// Important: clears component background
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int lowerBorder = getHeight() - fb.getHeight();
		int space = lowerBorder - 100 - 20;
		int barBorder = lowerBorder - space;

		// paint percentage bar
		g.setColor(Color.WHITE);
		g.fillRect(25, barBorder, getWidth() - (2 * FriendsBar.LEFT_BORDER), 10);

		// paint own player
		try {
			int percentagePosition = calculatePercentagePosition(ownScore.getPercentage());
			int pictureMiddle = Math.min(percentagePosition, getWidth() - FriendsBar.LEFT_BORDER
					- CustomButton.CARD_WIDTH / 2);
			pictureMiddle = Math.max(pictureMiddle, FriendsBar.LEFT_BORDER + CustomButton.CARD_WIDTH / 2);

			if (personButton != null) {
				personButton.setPicture(friendCash.getCurrentPlayer().getPicture());
				personButton.setScore(ownScore.getScore());
				personButton.setLocation(pictureMiddle - CustomButton.CARD_WIDTH / 2, 0);
				this.add(personButton);
			}

			drawPercentageLine(pictureMiddle, 100, percentagePosition, barBorder, g);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// paint line to all friends
		for (final Highscore highscore : highscores) {
			final Integer xPosition = fb.getXPosition(highscore.getPersonid());
			if (xPosition != null) {
				drawPercentageLine(xPosition, lowerBorder, calculatePercentagePosition(highscore.getPercentage()),
						barBorder + 10, g);
			}
		}

	}

	private int calculatePercentagePosition(int percentage) {
		return (int) (FriendsBar.LEFT_BORDER + (this.getWidth() - (2 * FriendsBar.LEFT_BORDER)) / 100.0 * percentage);
	}

	private void drawPercentageLine(int x1, int y1, int x2, int y2, Graphics g) {
		int half = y1 - (int) ((y1 - y2) / 2.0);
		if (CURVE) {
			CubicCurve2D.Float curve = new CubicCurve2D.Float(x1, y1, x1, half, x2, half, x2, y2);
			((Graphics2D) g).draw(curve);
		} else {
			g.drawLine(x1, y1, x1, half);
			g.drawLine(x1, half, x2, half);
			g.drawLine(x2, half, x2, y2);
		}
	}

}