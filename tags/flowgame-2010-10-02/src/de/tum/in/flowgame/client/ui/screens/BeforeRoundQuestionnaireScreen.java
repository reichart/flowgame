package de.tum.in.flowgame.client.ui.screens;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.tum.in.flowgame.client.ui.screens.story.RoundIntroScreen;

/**
 * Displays the short mood questionnaire before a game round.
 */
public class BeforeRoundQuestionnaireScreen extends QuestionnaireScreen {

	private final Action next = new AbstractAction(UIMessages.CONTINUE) {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveRoundAnswers(getAnswers(), getAnsweringTime());
			menu.show(RoundIntroScreen.class);
		}
	};

	@Override
	protected Action next() {
		return next;
	}
	
	@Override
	protected List<String> getQuestionnaireNames() {
		return Arrays.asList("moodShort");
	}
}
