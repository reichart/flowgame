package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.ProfileScreen;

/**
 * Displayed before going to the profile screen.
 */
public class BeforeProfileScreen extends MenuScreen {

	private final JButton next = goTo("Continue", ProfileScreen.class);

	@Override
	public Container getContents() {
		return centered(title("Before Profile"), next);
	}

}
