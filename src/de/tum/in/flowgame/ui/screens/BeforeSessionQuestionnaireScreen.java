package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.screens.story.RoundIntroScreen;

/**
 * Displays the mood and skills questionnaire at the beginning of a game
 * session.
 */
public class BeforeSessionQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(BeforeSessionQuestionnaireScreen.class);
	
	private final Action next = new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveSessionAnswers(getAnswers());
			menu.show(RoundIntroScreen.class);
		}
	};

	@Override
	protected Action next() {
		return next;
	}

	@Override
	protected List<Questionnaire> updateQuestionnaire(final GameLogic logic) throws IOException {
		log.info("creating new session, updating to before/scenariosession qn");
		logic.newSession();
		final List<Questionnaire> qs = new ArrayList<Questionnaire>();
		qs.add(logic.getCurrentScenarioSession().getQuestionnaire());
		return qs;
	}
}
