package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedString;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.json.JSONException;

import com.google.code.facebookapi.FacebookException;

import de.tum.in.flowgame.facebook.FacebookFriendCache;
import de.tum.in.flowgame.facebook.Friend;
import de.tum.in.flowgame.model.Highscore;

public class FriendsBar extends JPanel {

	private static final int MAX_NUMBER_OF_FRIENDS_SHOWN = 7;
	private static final int INNER_MARGIN = 10;
	private static final int MARGIN = 10;
	public static final int LEFT_BORDER = 25;

	private int currentPosition;

	private List<Highscore> highscores;
	private final Image cardBackground;
	private final FacebookFriendCache friendCash;

	private final ImageIcon iconLeftSmall = new ImageIcon(ImageIO.read(FriendsBar.class
			.getResource("/res/arrow_smaller_left.png")));
	private final ImageIcon iconLeftLarge = new ImageIcon(ImageIO.read(FriendsBar.class
			.getResource("/res/arrow_left.png")));

	private final ImageIcon iconRightSmall = new ImageIcon(ImageIO.read(FriendsBar.class
			.getResource("/res/arrow_smaller.png")));
	private final ImageIcon iconRightLarge = new ImageIcon(ImageIO.read(FriendsBar.class.getResource("/res/arrow.png")));

	public final int cardWidth;

	public FriendsBar(List<Highscore> highscores, FacebookFriendCache friendCash) throws Exception {
		this.friendCash = friendCash;
		update(highscores);

		URL url = FriendsBar.class.getResource("/res/picframe.png");
		cardBackground = ImageIO.read(url);
		cardWidth = cardBackground.getWidth(this) + MARGIN;

		this.setLayout(null);
		this.setPreferredSize(new Dimension(600, cardBackground.getHeight(this)));

		currentPosition = 0;

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
		btn.setLocation(0, 40);

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
					}
				}
				paintFriend(i - currentPosition, f.getPicture(), highscore.getScore(), g);
			} else {
				paintFriend(i - currentPosition, null, null, g);
			}
		}
	}

	private void paintFriend(int position, Image picture, Long score, Graphics g) {
		paintFriend(calculatePosition(position), 0, picture, score, g);
	}
	
	public void paintFriend(int x, int y, Image picture, Long score, Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(cardBackground, x, 0, this);
		g.drawImage(picture, x + INNER_MARGIN, INNER_MARGIN, this);

		if (score != null) {
			String scoreString = String.valueOf(score);
			AttributedString value = new AttributedString(scoreString);
			Font scoreFont = new Font("Arial", Font.BOLD, 14);
			value.addAttribute(TextAttribute.FONT, scoreFont);
			value.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.GRAY, 0, 10, Color.BLACK, true));

			FontMetrics metrics = getFontMetrics(scoreFont);

			g2d.drawString(value.getIterator(), x
					+ (cardBackground.getWidth(this) - metrics.stringWidth(scoreString)) / 2, 85);
		}
	}

	private int calculatePosition(int rank) {
		return rank * cardWidth + LEFT_BORDER;
	}

	public Integer getXPosition(long personid) {
		// Loop over displayed friends, if person is contained return x-position
		for (int i = 0; i < MAX_NUMBER_OF_FRIENDS_SHOWN; i++) {
			Highscore highscore = highscores.get(currentPosition + i);
			if (highscore.getPersonid() == personid) {
				return calculatePosition(i) + cardWidth / 2;
			}
		}
		return null;
	}

	public void update(List<Highscore> highscores) throws FacebookException, JSONException, IOException {
		friendCash.updateFriends();
		this.highscores = highscores;
	}

}