package de.tum.in.flowgame.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.google.code.facebookapi.FacebookException;

import de.tum.in.flowgame.facebook.FacebookFriendCache;
import de.tum.in.flowgame.facebook.Friend;
import de.tum.in.flowgame.model.Highscore;
import de.tum.in.flowgame.util.Browser;

public class FriendsBar extends JPanel {

	private final static Log log = LogFactory.getLog(FriendsBar.class);
	
	private static final int MAX_NUMBER_OF_FRIENDS_SHOWN = 7;
	public static final int INNER_MARGIN = 10;
	public static final int LEFT_BORDER = 25;

	private int currentPosition;

	private List<Highscore> highscores;
	private final FacebookFriendCache friendCash;

	private final ImageIcon iconLeftSmall = new ImageIcon(ImageIO.read(FriendsBar.class
			.getResource("/res/arrow_smaller_left.png")));
	private final ImageIcon iconLeftLarge = new ImageIcon(ImageIO.read(FriendsBar.class
			.getResource("/res/arrow_left.png")));

	private final ImageIcon iconRightSmall = new ImageIcon(ImageIO.read(FriendsBar.class
			.getResource("/res/arrow_smaller.png")));
	private final ImageIcon iconRightLarge = new ImageIcon(ImageIO.read(FriendsBar.class.getResource("/res/arrow.png")));

	private final List<CustomButton> friendButtons;

	public FriendsBar(List<Highscore> highscores, FacebookFriendCache friendCash, Browser browser) throws Exception {
		this.friendCash = friendCash;

		friendButtons = new ArrayList<CustomButton>();
		update(highscores);

		this.setLayout(null);
		this.setPreferredSize(new Dimension(600, CustomButton.CARD_HEIGHT));

		currentPosition = 0;
		
		for (int i = 0; i < MAX_NUMBER_OF_FRIENDS_SHOWN; i++) {
			final CustomButton btn = new CustomButton(browser);
			btn.setLocation(calculatePosition(i), 0);
			this.add(btn);
			friendButtons.add(btn);
		}

		final JButton leftArrow = createLeftButton();
		this.add(leftArrow);

		final JButton rightArrow = createRightButton();
		this.add(rightArrow);

	}

	private JButton createLeftButton() {
		final JButton btn = new JButton(iconLeftSmall);
		btn.setBorderPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setSize(25, 25);
		btn.setContentAreaFilled(false);
		btn.setLocation(5, 40);

		btn.setText("Invite Friends");

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentPosition > 0) {
					currentPosition--;
				}
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setIcon(iconLeftSmall);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setIcon(iconLeftLarge);
			}

		});
		return btn;
	}

	private JButton createRightButton() {
		final JButton btn = new JButton(iconRightSmall);
		btn.setBorderPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setSize(25, 25);
		btn.setContentAreaFilled(false);
		btn.setLocation(575, 40);

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentPosition < highscores.size() - MAX_NUMBER_OF_FRIENDS_SHOWN) {
					currentPosition++;
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setIcon(iconRightSmall);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setIcon(iconRightLarge);
			}

		});
		return btn;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		getParent().repaint();

		for (int i = currentPosition; i < currentPosition + MAX_NUMBER_OF_FRIENDS_SHOWN; i++) {
			if (highscores.size() > i) {
				Highscore highscore = highscores.get(i);
				Friend f = null;
				for (Friend friend : friendCash.getFriends()) {
					if (highscore.getPersonid() == friend.getId()) {
						f = friend;
						break;
					}
				}
				if (f != null) {
					friendButtons.get(i).setPicture(f.getPicture());
					friendButtons.get(i).setScore(highscore.getScore());
				} else {
					log.warn("Friend Object could not be found");
				}
			} else {
				friendButtons.get(i).setPicture(null);
				friendButtons.get(i).setScore(null);
			}
		}
	}

	private int calculatePosition(int rank) {
		return rank * CustomButton.CARD_WIDTH + LEFT_BORDER;
	}

	public Integer getXPosition(long personid) {
		// Loop over displayed friends, if person is contained return x-position
		for (int i = 0; i < MAX_NUMBER_OF_FRIENDS_SHOWN; i++) {
			Highscore highscore = highscores.get(currentPosition + i);
			if (highscore.getPersonid() == personid) {
				return calculatePosition(i) + CustomButton.CARD_WIDTH / 2;
			}
		}
		return null;
	}

	public void update(List<Highscore> highscores) throws FacebookException, JSONException {
		friendCash.updateFriends();
		this.highscores = highscores;
	}

}