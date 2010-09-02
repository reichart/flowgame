package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.ProfileScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed before going to the profile screen.
 */
public class BeforeProfileScreen extends StoryScreen {

	private final JButton next = goTo(UIMessages.CONTINUE, ProfileScreen.class);

	@Override
	public Container getContents() {
		return centered(COMMON_BORDER, title(UIMessages.getString("before_profile")), text, next);
	}

	@Override
	protected String getText() {
		return UIMessages.getString("before_profile.text");
	}

}
