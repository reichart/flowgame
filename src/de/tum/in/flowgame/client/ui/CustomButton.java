package de.tum.in.flowgame.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.client.ui.screens.UIMessages;
import de.tum.in.flowgame.client.util.Browser;

public class CustomButton extends JButton {

	private final static Log log = LogFactory.getLog(CustomButton.class);

	private Image picture;
	private Long score;
	private final Font font;
	private final FontMetrics metrics;
	private final Browser browser;

	private static final int MARGIN = 10;
	private static final Image cardBackground = loadCardBackground();

	public static final int CARD_WIDTH = cardBackground.getWidth(null) + MARGIN;
	public static final int CARD_HEIGHT = cardBackground.getHeight(null);

	private static Image loadCardBackground() {
		Image img;
		try {
			img = ImageIO.read(FriendsBar.class.getResource("/res/picframe.png"));
		} catch (final Exception e) {
			img = new BufferedImage(70, 100, BufferedImage.TYPE_4BYTE_ABGR);
			img = makeColorTransparent(img, Color.BLACK);
			log.warn("Buttons have no background image.");
		}
		return img;
	}

	private static Image makeColorTransparent(Image im, final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			// the color we are looking for... Alpha bits are set to opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else {
					// nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private CustomButton(Image picture, Long score, Browser browser) {

		this.picture = picture;
		this.score = score;
		this.browser = browser;

		font = new Font("Arial", Font.BOLD, 14);
		metrics = getFontMetrics(font);

		setBorderPainted(false);
		setBorder(BorderFactory.createEmptyBorder());

		setSize(cardBackground.getWidth(this), cardBackground.getHeight(this));
		setContentAreaFilled(false);

		addMouseListener();
	}

	public CustomButton() {
		this(null, null);
	}

	private CustomButton(Image picture, Long score) {
		this(picture, score, null);
	}

	public CustomButton(Browser browser) {
		this(null, null, browser);
	}

	private void addMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (score == null) {
					browser.toggleInviteDialog();
				}
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(cardBackground, 0, 0, this);
		g.drawImage(picture, FriendsBar.INNER_MARGIN, FriendsBar.INNER_MARGIN, this);

		if (score != null) {
			String valueOf = String.valueOf(score);
			g2d.drawString(getAttributedString(valueOf).getIterator(), (cardBackground.getWidth(this) - metrics.stringWidth(valueOf)) / 2, 85);
		} else {
			String[] texts = (UIMessages.getString("friend.invite")).split(" ");
			String text1 = texts[0];
			g2d.drawString(getAttributedString(text1).getIterator(), (cardBackground.getWidth(this) - metrics.stringWidth(text1)) / 2, 40);
			if (texts.length > 1) {
				String text2 = texts[1];
				g2d.drawString(getAttributedString(text2).getIterator(), (cardBackground.getWidth(this) - metrics.stringWidth(text2)) / 2,
						40 + metrics.getHeight());
			}
		}
	}

	private AttributedString getAttributedString(String text) {
		AttributedString dText1 = new AttributedString(text);
		dText1.addAttribute(TextAttribute.FONT, font);
		dText1.addAttribute(TextAttribute.FOREGROUND, new GradientPaint(0, 0, Color.GRAY, 0, 10, Color.BLACK, true));
		return dText1;
	}

	public void setPicture(Image picture) {
		this.picture = picture;
		repaint();
	}

	public void setScore(Long score) {
		this.score = score;
		repaint();
	}

}