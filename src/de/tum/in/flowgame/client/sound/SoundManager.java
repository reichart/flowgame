package de.tum.in.flowgame.client.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {

	/**
	 * Maximum number of sounds being played simultanously (including music).
	 */
	private static final int NUM_SOUNDS = 4;

	private static final SoundManager instance = new SoundManager();

	private final Map<Sound, AudioStream> streams;

	private final ExecutorService executor;

	private boolean muted;

	public static SoundManager getInstance() {
		return instance;
	}

	public SoundManager() {
		this.streams = new HashMap<Sound, AudioStream>();
		this.executor = Executors.newFixedThreadPool(NUM_SOUNDS);
	}

	public void loop(final Sound sound) {
		start(sound, true);
	}

	public void once(final Sound sound) {
		start(sound, false);
	}

	private void start(final Sound sound, final boolean loop) {
		if (muted) {
			return;
		}

		final AudioStream stream = new AudioStream(sound, loop);

		// stop previous before playing new sound
		final AudioStream previous = streams.put(sound, stream);
		if (previous != null) {
			previous.stop(false);
		}

		executor.submit(stream);
	}

	public void stop(final Sound sound, final boolean wait) {
		final AudioStream stream = streams.remove(sound);
		if (stream != null) {
			stream.stop(wait);
		}

	}

	public void pause() {
		for (final AudioStream stream : streams.values()) {
			stream.pause();
		}
	}

	public void unpause() {
		for (final AudioStream stream : streams.values()) {
			stream.unpause();
		}
	}

	public void setMuted(final boolean muted) {
		this.muted = muted;

		for (final AudioStream stream : streams.values()) {
			stream.setMuted(muted);
		}
	}
}
