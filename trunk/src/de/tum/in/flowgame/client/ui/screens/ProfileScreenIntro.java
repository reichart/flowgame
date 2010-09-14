package de.tum.in.flowgame.client.ui.screens;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.story.StoryScreen;
import de.tum.in.flowgame.model.Questionnaire;

/**
 * Displays a profile {@link Questionnaire} to assess the player's personality. 
 */
public class ProfileScreenIntro extends StoryScreen {
	
	@Override
	protected JButton next() {
		return goTo(UIMessages.CONTINUE, ProfileScreen.class);
	}
	
	@Override
	protected String getTitleKey() {
		return "before_profile";
	}

	@Override
	protected String getTextKey() {
		return "profile";
	}
	
}