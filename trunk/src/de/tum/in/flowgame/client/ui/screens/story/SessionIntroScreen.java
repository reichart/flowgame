package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.BeforeSessionQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.MenuScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed when going from the main menu to the initial session questionnaire.
 */
public class SessionIntroScreen extends MenuScreen {

	private final JButton next = goTo(UIMessages.CONTINUE, BeforeSessionQuestionnaireScreen.class);

	@Override
	public Container getContents() {
		return centered(title(UIMessages.getString("session.intro")), next);
	}

}
