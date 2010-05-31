package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.tum.in.flowgame.ui.screens.story.RoundExtroScreen;

/**
 * Displays the "how was it" questionnaire after the highscores.
 */
public class AfterRoundQuestionnaireScreen extends QuestionnaireScreen {

	private final Action next = new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveRoundAnswers(getAnswers());
			menu.show(RoundExtroScreen.class);
		}
	};

	@Override
	protected Action next() {
		return next;
	}

	@Override
	protected List<String> getQuestionnaireNames() {
		return Arrays.asList("moodShort", "howWasIt");
	}
}