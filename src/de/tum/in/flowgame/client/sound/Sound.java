package de.tum.in.flowgame.client.sound;

import java.io.InputStream;

public enum Sound {
	/**
	 * Music played during menus and questionnaires.
	 */
	MENU("menuloop", true),

	/**
	 * Music played during game play.
	 */
	GAMEPLAY("gameplay", true),

	/**
	 * Sound when colliding with an asteroid.
	 */
	ASTEROID("asteroid", false),

	/**
	 * Sound when colliding with a goodie.
	 */
	GOODIE("goodie", false);

	private final String resource;

	/**
	 * If <code>false</code>, the sound system will not mute this sound but let
	 * it play out. Use this only for very short sounds.
	 */
	private boolean mutable;

	private Sound(final String name, final boolean mutable) {
		this.resource = "/res/sound/" + name + ".ogg";
		this.mutable = mutable;
	}

	public boolean isMutable() {
		return mutable;
	}

	public InputStream load() {
		return getClass().getResourceAsStream(resource);
	}
}
