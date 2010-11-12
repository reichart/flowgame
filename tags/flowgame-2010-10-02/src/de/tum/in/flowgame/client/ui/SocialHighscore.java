package de.tum.in.flowgame.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.client.facebook.FacebookFriendCache;
import de.tum.in.flowgame.model.Highscore;

public class SocialHighscore extends JPanel {

	private static final boolean CURVE = true;
	private static final Log log = LogFactory.getLog(SocialHighscore.class);

	private final FacebookFriendCache friendCash;
	private FriendsBar fb;
	private Highscore ownScore;
	private List<Highscore> highscores;
	private CustomButton personButton;
	
	private GameLogic logic;

	public SocialHighscore(final FacebookFriendCache friendCash, GameLogic logic) {
		this.logic = logic;
		this.friendCash = friendCash;

		try {
			this.setLayout(new BorderLayout());
			fb = new FriendsBar(highscores, friendCash, logic.getBrowser());
			this.add(fb, BorderLayout.SOUTH);
		} catch (Exception ex) {
			fb = null;
			log.error("failed to update social highscore", ex);
		}

		personButton = new CustomButton();
	}

	public void update() throws Exception {
		friendCash.updateFriends();
		updateHighscores();
	}

	private void updateHighscores() throws Exception {
		final List<Long> persons = friendCash.getFriendsids();

		final Client client = logic.getClient();
		if (client == null) {
			log.warn("no client, not updating highscore");
			return;
		}

		final long playerId = friendCash.getCurrentPlayer().getId();
		
		final boolean showHighscores = logic.getCurrentGameRound() == null;
		if (showHighscores) {
			log.info("displaying social high scores");
			
			persons.add(friendCash.getCurrentPlayer().getId());

			final List<Highscore> globalScores = client.getHighscores(persons);
			for (final Highscore highscore : globalScores) {
				if (highscore.getPersonid() == playerId) {
					ownScore = highscore;
					break;
				}
			}
			globalScores.remove(ownScore);
			highscores = globalScores;
		} else {
			log.info("displaying social current score");
			
			highscores = client.getHighscores(persons);
			final long currentScore = logic.getScore();
			final Integer percentage = client.getPercentage(new Highscore(playerId, currentScore));
			ownScore = new Highscore(playerId, currentScore, percentage);
		}
		
		log.info("own score " + ownScore);
		
		fb.update(highscores);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		// Important: clears component background
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (fb == null) {
			log.error("Friends Bar is null");
			return;
		}

		int lowerBorder = getHeight() - fb.getHeight();
		int space = lowerBorder - 100 - 20;
		int barBorder = lowerBorder - space;

		// paint percentage bar
		g.setColor(Color.WHITE);
		g.fillRect(25, barBorder, getWidth() - (2 * FriendsBar.LEFT_BORDER), 10);

		if (ownScore != null) {
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