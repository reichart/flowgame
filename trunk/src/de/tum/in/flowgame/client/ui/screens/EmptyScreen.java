package de.tum.in.flowgame.client.ui.screens;

import java.awt.Container;

/**
 * Empty menu to avoid artifacts when switching the menu back on after a game.
 * Without an empty menu, the previous screen could briefly be seen.
 */
public class EmptyScreen extends MenuScreen {

	@Override
	public Container getContents() {
		return new Container();
	}

}
