package de.tum.in.flowgame.client.sound;

import java.io.InputStream;

public enum Sound {
	/**
	 * Music played during menus and questionnaires.
	 */
	MENU("menuloop"),

	/**
	 * Music played during game play.
	 */
	GAMEPLAY("gameplay"),

	/**
	 * Sound when colliding with an asteroid.
	 */
	ASTEROID("asteroid"),

	/**
	 * Sound when colliding with a goodie.
	 */
	GOODIE("goodie");

	private final String resource;

	private Sound(final String name) {
		this.resource = "/res/sound/" + name + ".ogg";
	}

	public InputStream load() {
		return getClass().getResourceAsStream(resource);
	}
}
