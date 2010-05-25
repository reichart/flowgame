package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.GameMenu;

/**
 * Displays the "how was it" questionnaire after the highscores.
 */
public class AfterRoundQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(AfterRoundQuestionnaireScreen.class);
	
	private final Action next = new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveRoundAnswers(getAnswers());
			menu.show(GameSessionExtroScreen.class);
		}
	};

	public AfterRoundQuestionnaireScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	protected Action next() {
		return next;
	}

	@Override
	protected Questionnaire updateQuestionnaire(final GameLogic logic) {
		log.info("updating to after/scenarioround qn");
		return logic.getCurrentScenarioRound().getQuestionnaire();
	}
}
