package de.tum.in.flowgame;

import java.applet.Applet;
import java.applet.AudioClip;

public enum Sounds {

	ASTEROID("crash"), FUELCAN("water-droplet-1"), DEATH("chewie");

	private final AudioClip snd;

	private Sounds(final String res) {
		this.snd = Applet.newAudioClip(getClass().getResource("/res/sound/" + res + ".wav"));
	}

	public void play() {
		snd.play();
	}

}