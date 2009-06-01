package de.tum.in.flowgame;

import java.applet.Applet;
import java.applet.AudioClip;

public class NoiseMaker {

	private AudioClip asteroidSound;

	public NoiseMaker() {
		this.asteroidSound = Applet.newAudioClip(getClass().getResource("/res/crash.wav"));
	}

	public void playSound(String soundName) {
		if (GameLogic.ASTEROID.equals(soundName)) {
			asteroidSound.play();
		}
	}

}