package de.tum.in.flowgame.client.ui.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSprite implements Sprite {

	private final BufferedImage image;

	public ImageSprite(final String resource) throws IOException {
		this.image = ImageIO.read(ImageSprite.class.getResource(resource));
	}

	public void render(final Graphics2D g, final int x, final int y) {
		g.drawImage(image, x, y, null);
	}

	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		g.drawImage(image, x, y, w, h, null);
	}
}
