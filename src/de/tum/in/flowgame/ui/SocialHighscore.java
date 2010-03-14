package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.facebook.FacebookFriendCache;
import de.tum.in.flowgame.model.Highscore;

public class SocialHighscore extends JPanel {

	private static final boolean STRAIGHT = true;
	
	private final FacebookFriendCache friendCash;
	private final FriendsBar fb;
	private Highscore ownScore;
	private List<Highscore> highscores;
	private final Client client;

	public SocialHighscore(final Client client,  final FacebookFriendCache friendCash) {
		this.client = client;
		this.friendCash = friendCash;
		
		try {
			update();
			
			this.setLayout(new BorderLayout());		
			fb = new FriendsBar(highscores, friendCash);
			this.add(fb, BorderLayout.SOUTH);
		} catch (Exception ex) {
			// TODO handle exception
			throw new RuntimeException(ex);
		}
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
//		globalScores.remove(ownScore);
		highscores = globalScores;
	}
	
	
	@Override
	protected void paintComponent(final Graphics g) {
		//Important: clears component background
		super.paintComponent(g);

		int lowerBorder = getHeight() - fb.getHeight();
		int space = lowerBorder - 100 - 20;
		int barBorder = lowerBorder - space;

		//paint percentage bar
		g.setColor(Color.BLUE);
		g.fillRect(25, barBorder, 550, 10);
		
		//paint own player
		try {
			int percentagePosition = (int) (25 +  550/100.0 * ownScore.getPercentage());
			fb.paintFriend(percentagePosition - 35, 0, friendCash.getCurrentPlayer().getPicture(), ownScore.getScore(), g);
			drawPercentageLine(percentagePosition, 100, percentagePosition, barBorder, g);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//paint line to all friends
		for (final Highscore highscore : highscores) {
			final Integer xPosition = fb.getXPosition(highscore.getPersonid());
			if (xPosition != null) {
				drawPercentageLine(xPosition, lowerBorder, (int) (25 +  550/100.0 * highscore.getPercentage()), barBorder + 10, g);
			}
		}
		
	}
	
	private void drawPercentageLine(int x1, int y1, int x2, int y2, Graphics g) {
		if (STRAIGHT) {
			g.drawLine(x1, y1, x2, y2);
		} else {
			int half = y1 - (int) ((y1-y2) / 2.0);
			g.drawLine(x1, y1, x1, half);
			g.drawLine(x1, half, x2, half);
			g.drawLine(x2, half, x2, y2);
		}
	}
	
}