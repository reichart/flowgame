package de.tum.in.flowgame.client.ui.screens.story;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.ProfileScreenIntro;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed before going to the profile screen.
 */
public class BeforeProfileScreen extends StoryScreen {

	@Override
	protected JButton next() {
		return goTo(UIMessages.CONTINUE, ProfileScreenIntro.class);
	}

	@Override
	protected String getTitleKey() {
		return "before_profile";
	}

	@Override
	protected String getTextKey() {
		return "before_profile.text";
	}

}
