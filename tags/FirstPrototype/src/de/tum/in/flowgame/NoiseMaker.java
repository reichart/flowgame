package de.tum.in.flowgame;

import java.applet.Applet;
import java.applet.AudioClip;

public class NoiseMaker {

	private AudioClip asteroidSound;
	private AudioClip fuelSound;

	public NoiseMaker() {
		this.asteroidSound = Applet.newAudioClip(getClass().getResource("/res/sound/crash.wav"));
		this.fuelSound = Applet.newAudioClip(getClass().getResource("/res/sound/water-droplet-1.wav"));
	}

	public void playSound(String soundName) {
		if (GameLogic.ASTEROID.equals(soundName)) {
			asteroidSound.play();
		}
		if (GameLogic.FUELCAN.equals(soundName)) {
			fuelSound.play();
		}
	}

}