package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.screens.story.RoundExtroScreen;

/**
 * Displays the "how was it" questionnaire after the highscores.
 */
public class AfterRoundQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(AfterRoundQuestionnaireScreen.class);
	
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
	protected List<Questionnaire> updateQuestionnaire(final GameLogic logic) {
		log.info("updating to after/scenarioround qn");
		return logic.getCurrentScenarioRound().getQuestionnaires();
	}
}
