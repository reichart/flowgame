package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.MainScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed after coming from the profile screen going to main menu.
 */
public class AfterProfileScreen extends StoryScreen {

	private final JButton next = goTo(UIMessages.CONTINUE, MainScreen.class);

	@Override
	public Container getContents() {
		return centered(COMMON_BORDER, title(UIMessages.getString("after_questionnaire")), next);
	}

	@Override
	protected String getText() {
		return UIMessages.getString("after_profile.text");
	}

}
