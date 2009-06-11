package de.tum.in.flowgame;

import java.applet.Applet;
import java.applet.AudioClip;

import de.tum.in.flowgame.GameLogic.Item;

public class NoiseMaker {

	private AudioClip asteroidSound;
	private AudioClip fuelSound;

	public NoiseMaker() {
		this.asteroidSound = Applet.newAudioClip(getClass().getResource("/res/sound/crash.wav"));
		this.fuelSound = Applet.newAudioClip(getClass().getResource("/res/sound/water-droplet-1.wav"));
	}

	public void playSound(Item items) {
		if (Item.ASTEROID == items) {
			asteroidSound.play();
		}
		if (Item.FUELCAN == items) {
			fuelSound.play();
		}
	}

}