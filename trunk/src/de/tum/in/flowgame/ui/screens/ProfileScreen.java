package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.screens.story.AfterProfileScreen;

/**
 * Displays a profile {@link Questionnaire} to assess the player's personality. 
 */
public class ProfileScreen extends QuestionnaireScreen {
	
	private final Action createProfile = new AbstractAction(UIMessages.CONTINUE) {
		public void actionPerformed(final ActionEvent e) {
			final Person player = menu.getLogic().getPlayer();
			player.setProfilingAnswers(getAnswers());
			menu.getLogic().getClient().uploadQuietly(player);
			menu.show(AfterProfileScreen.class);
		}
	};
	
	@Override
	protected Action next() {
		return createProfile;
	}

	@Override
	protected List<String> getQuestionnaireNames() {
		return Arrays.asList("profile");
	}
}