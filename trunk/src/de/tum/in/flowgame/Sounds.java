package de.tum.in.flowgame;

import java.applet.AudioClip;

public enum Sounds {

	ASTEROID("crash"), FUELCAN("water-droplet-1"), DEATH("chewie");

	private final AudioClip snd;

	private Sounds(final String res) {
		this.snd = SoundsHelper.getApplet().getAudioClip(getClass().getResource("/res/sound/" + res + ".wav"));
		
	}

	public void play() {
		if (snd != null) {
			snd.play();
		}
	}

}