package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;

/**
 * Displays the mood and skills questionnaire at the beginning of a game
 * session.
 */
public class BeforeSessionQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(BeforeSessionQuestionnaireScreen.class);
	
	private final Action next = new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveSessionAnswers(getAnswers());
			menu.show(GameSessionIntroScreen.class);
		}
	};

	@Override
	protected Action next() {
		return next;
	}

	@Override
	protected Questionnaire updateQuestionnaire(final GameLogic logic) throws IOException {
		log.info("creating new session, updating to before/scenariosession qn");
		logic.newSession();
		return logic.getCurrentScenarioSession().getQuestionnaire();
	}
}
