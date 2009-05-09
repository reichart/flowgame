package j3DGame;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SpriteCache {
	
	
	private static SpriteCache instance;

	private final Map<String, BufferedImage> sprites;

	private final BufferedImage errorImage;

	private SpriteCache() {
		this.sprites = new HashMap<String, BufferedImage>();
		this.errorImage = createErrorImage(64, 64);
	}

	private BufferedImage createErrorImage(final int width, final int height) {
		final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics g = img.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.RED);
		g.drawLine(0, 0, width, height);
		g.drawLine(0, height, width, 0);
		
		g.dispose();
		
		return img;
	}

	public synchronized static SpriteCache getInstance() {
		if (instance == null) {
			instance = new SpriteCache();
		}

		return instance;
	}

	private BufferedImage loadImage(final String name) {
		BufferedImage result;
		
		try {
			result = ImageIO.read(getClass().getResource(name));
		} catch (final Exception ex) {
			result = errorImage;
		}
		
		return result;
	}

	public BufferedImage getSprite(final String name) {
		BufferedImage img = sprites.get(name);
		if (img == null) {
			img = loadImage("/res/" + name);
			sprites.put(name, img);
		}
		
		return (img == null) ? errorImage : img;
	}
}