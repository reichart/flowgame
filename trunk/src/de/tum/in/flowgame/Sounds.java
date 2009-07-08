package de.tum.in.flowgame;

import net.java.games.sound3d.Source;
import de.tum.in.flowgame.util.OALUtil;

public enum Sounds {

	ASTEROID("crash"), FUELCAN("water-droplet-1"), DEATH("chewie");

	private final Source snd;

	private Sounds(final String res) {
		this.snd = OALUtil.loadSound("/res/sound/" + res + ".wav");
	}

	public void play() {
		if (snd != null) {
			snd.play();
		}
	}

	private void destroy() {
		if (snd != null) {
			snd.delete();
		}
	}

	public static void close() {
		for (final Sounds sounds : values()) {
			sounds.destroy();
		}
	}
}