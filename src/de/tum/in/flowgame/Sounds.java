package de.tum.in.flowgame;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.AL;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;
import net.java.games.joal.util.ALut;
import net.java.games.sound3d.AudioSystem3D;
import net.java.games.sound3d.Buffer;
import net.java.games.sound3d.Source;

public enum Sounds {

	ASTEROID("crash"), FUELCAN("water-droplet-1"), DEATH("chewie");

	private Source snd = null;
	
	static boolean initialized = false;
	
	private Sounds(final String res) {
		initialize();
		URL url = getClass().getResource("/res/sound/" + res + ".wav");
		InputStream input;
		try {
			input = url.openStream();
			AudioSystem3D as = new AudioSystem3D();
			as.init();
//			as.generateBuffers(1);
			Buffer buffer = as.loadBuffer(input);
			snd = as.generateSource(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		snd.play();
	}

	public void destroy() {
		snd.delete();
	}

	public static void destroyAll() {
		for (final Sounds sounds : values()) {
			sounds.destroy();
		}
	}
	
	private static boolean initialize() {
		
		if (initialized) {
			return true;
		}

		// Initialize OpenAL and clear the error bit.
		try {
			ALut.alutInit();
			AL al = ALFactory.getAL();
			al.alGetError();
		} catch (ALException e) {
			e.printStackTrace();
			return false;
		}
		
		initialized = true;
		return true;
	}

	private void killAllData() {
//		al.alDeleteBuffers(1, buffer, 0);
//		al.alDeleteSources(1, source, 0);
	}
}