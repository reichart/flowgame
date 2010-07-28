package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.UIMessages;

/**
 * Displayed after coming from the profile screen going to main menu.
 */
public class AfterProfileScreen extends MenuScreen {

	private final JButton next = goTo(UIMessages.CONTINUE, MainScreen.class);

	@Override
	public Container getContents() {
		return centered(title(UIMessages.getString("after_questionnaire")), next);
	}

}
