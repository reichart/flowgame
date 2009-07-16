package de.tum.in.flowgame.util;

import java.io.InputStream;

import net.java.games.joal.ALException;
import net.java.games.joal.util.ALut;
import net.java.games.sound3d.AudioSystem3D;
import net.java.games.sound3d.Buffer;
import net.java.games.sound3d.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OALUtil {

	private static final Log log = LogFactory.getLog(OALUtil.class);

	static {
		try {
			ALut.alutInit();
			AudioSystem3D.init();
		} catch (final ALException ex) {
			log.error("failed to initialize OpenAL", ex);
		}
	}

	public static Source loadSound(final String resource) {
		try {
			final InputStream input = OALUtil.class.getResourceAsStream(resource);
			final Buffer buffer = AudioSystem3D.loadBuffer(input);
			return AudioSystem3D.generateSource(buffer);
		} catch (final Exception ex) {
			log.error("failed to load sound " + resource, ex);
			return null;
		}
	}
}
