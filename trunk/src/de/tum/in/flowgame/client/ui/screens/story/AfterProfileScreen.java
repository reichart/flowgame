package de.tum.in.flowgame.client.ui.screens.story;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.MainScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed after coming from the profile screen going to main menu.
 */
public class AfterProfileScreen extends StoryScreen {

	@Override
	protected JButton next() {
		return goTo(UIMessages.CONTINUE, MainScreen.class);
	}

	@Override
	protected String getTitleKey() {
		return "after_questionnaire";
	}

	@Override
	protected String getTextKey() {
		return "after_profile.text";
	}

}
