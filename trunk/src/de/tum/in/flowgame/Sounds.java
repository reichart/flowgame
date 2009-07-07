package de.tum.in.flowgame;

import net.java.games.sound3d.Source;
import de.tum.in.flowgame.util.OALUtil;

public enum Sounds {

	ASTEROID("crash"), FUELCAN("water-droplet-1"), DEATH("chewie");

	private final Source snd;

	private Sounds(final String res) {
		try {
			this.snd = OALUtil.loadSound("/res/sound/" + res + ".wav");
		} catch (final Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void play() {
		snd.play();
	}

	private void destroy() {
		snd.delete();
	}

	public static void close() {
		for (final Sounds sounds : values()) {
			sounds.destroy();
		}
	}
}