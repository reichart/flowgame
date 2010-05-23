package de.tum.in.flowgame;

import java.applet.AudioClip;

import org.apache.commons.logging.LogFactory;

public enum Sounds {

	ASTEROID("crash"), FUELCAN("water-droplet-1"), DEATH("chewie");

	private AudioClip snd;

	private Sounds(final String res) {
		try {
			this.snd = SoundsHelper.getApplet().getAudioClip(getClass().getResource("/res/sound/" + res + ".wav"));
		} catch (final Exception ex) {
			LogFactory.getLog(Sounds.class).warn("failed to load sound " + res, ex);
			this.snd = null;
		}
	}

	public void play() {
		if (snd != null) {
			snd.play();
		}
	}

}