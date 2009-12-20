package de.tum.in.flowgame.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Singleton to load all sprites through one location and avoid multiple
 * instances of the same sprite.
 */
public class SpriteCache {

	private final static Log log = LogFactory.getLog(SpriteCache.class);

	private static final SpriteCache instance = new SpriteCache();

	private final Map<String, Sprite> sprites;

	public SpriteCache() {
		this.sprites = new HashMap<String, Sprite>();
	}

	public static SpriteCache getInstance() {
		return instance;
	}

	public Sprite getSprite(final String resource) {
		Sprite sprite = sprites.get(resource);
		if (sprite == null) {
			sprite = new CachedSprite(createSprite(resource));
			sprites.put(resource, sprite);
		}

		return sprite;
	}

	private Sprite createSprite(final String resource) {
		try {
			if (resource.toLowerCase().endsWith(".svg")) {
				return new SVGSprite(resource);
			} else {
				return new ImageSprite(resource);
			}
		} catch (final Exception ex) {
			log.error("failed to load sprite " + resource, ex);
			return null;
		}
	}
}
