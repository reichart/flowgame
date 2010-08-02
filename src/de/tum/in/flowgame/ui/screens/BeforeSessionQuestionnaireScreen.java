package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.ui.GameMenu;

public class BeforeSessionQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(BeforeSessionQuestionnaireScreen.class);
	
	private final JButton play = new JButton(new AbstractAction("Play!") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveSessionAnswers(getAnswers());
			
			log.info("starting first round");
			final ScenarioSession scenarioSession = menu.getLogic().getCurrentScenarioSession();
			menu.getLogic().start(scenarioSession.getNextRound());
		}
	});

	public BeforeSessionQuestionnaireScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	protected JButton nextButton() {
		return play;
	}

	@Override
	protected Questionnaire updateQuestionnaire(final GameLogic logic) throws IOException {
		log.info("creating new session, updating to before/scenariosession qn");
		logic.newSession();
		return logic.getCurrentScenarioSession().getQuestionnaire();
	}
}
