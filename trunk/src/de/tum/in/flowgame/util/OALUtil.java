package de.tum.in.flowgame.util;

import java.io.InputStream;

import net.java.games.joal.AL;
import net.java.games.joal.ALFactory;
import net.java.games.joal.util.ALut;
import net.java.games.sound3d.AudioSystem3D;
import net.java.games.sound3d.Buffer;
import net.java.games.sound3d.Source;

public class OALUtil {

	static {
		ALut.alutInit();
		final AL al = ALFactory.getAL();
		al.alGetError();
		AudioSystem3D.init();
	}

	public static Source loadSound(final String resource) throws Exception {
		final InputStream input = OALUtil.class.getResourceAsStream(resource);
		final Buffer buffer = AudioSystem3D.loadBuffer(input);
		return AudioSystem3D.generateSource(buffer);
	}
}
