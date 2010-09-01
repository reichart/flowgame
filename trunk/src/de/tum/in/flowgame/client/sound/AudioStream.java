package de.tum.in.flowgame.client.sound;

import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;

/**
 * An audio stream using the Java Sound API.
 */
public class AudioStream implements Runnable {

	private enum State {
		PLAY, STOP
	}

	private final static Log log = LogFactory.getLog(AudioStream.class);

	private final Sound sound;

	private final boolean loop;

	private volatile State state;

	private volatile CountDownLatch finished, paused;

	public AudioStream(final Sound sound, final boolean loop) {
		this.sound = sound;
		this.loop = loop;
		this.state = State.PLAY;
	}

	public Sound getSound() {
		return sound;
	}

	public void run() {
		finished = new CountDownLatch(1);

		for (int i = 0; state == State.PLAY && (loop || i == 0); i++) {
			AudioInputStream in = null;
			AudioInputStream din = null;
			try {
				in = AudioSystem.getAudioInputStream(sound.load());
				final AudioFormat decodedFormat = decoded(in.getFormat());
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				final SourceDataLine line = getLine(decodedFormat);

				line.start();

				final byte[] buf = new byte[1024];
				int len = 0;
				while ((len = din.read(buf, 0, buf.length)) != -1) {
					if (state == State.STOP) {
						break;
					}
					if (paused != null) {
						paused.await();
						paused = null;
					}
					line.write(buf, 0, len);
				}
				line.drain();
				line.stop();
				line.close();
			} catch (final Exception ex) {
				log.warn("failed to stream " + this, ex);
				break;
			} finally {
				Utils.closeQuietly(din);
				Utils.closeQuietly(in);
			}
		}
		finished.countDown();
	}

	public void play() {
		state = State.PLAY;
	}

	public void stop(final boolean wait) {
		state = State.STOP;
		if (wait) {
			try {
				finished.await();
			} catch (final InterruptedException e) {
				// ignore
			}
		}
	}

	public void pause() {
		paused = new CountDownLatch(1);
	}

	public void unpause() {
		paused.countDown();
	}

	/**
	 * Creates an decoded format from an encoded input.
	 */
	private static AudioFormat decoded(final AudioFormat input) {
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, input.getSampleRate(), 16, input.getChannels(), input
				.getChannels() * 2, input.getSampleRate(), false);
	}

	/**
	 * Gets a "line" for output on the sound device.
	 */
	private static SourceDataLine getLine(final AudioFormat audioFormat) throws LineUnavailableException {
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		final SourceDataLine res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

}
