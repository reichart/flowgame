package de.tum.in.flowgame.ui.screens;

import java.awt.Container;

import de.tum.in.flowgame.ui.GameMenu;

/**
 * Empty menu to avoid artifacts when switching the menu back on after a game.
 * Without an empty menu, the previous screen could briefly be seen.
 */
public class EmptyScreen extends MenuScreen {

	public EmptyScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return new Container();
	}

}
