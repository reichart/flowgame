package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.ui.GameMenu;

public class AfterRoundQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(AfterRoundQuestionnaireScreen.class);
	
	private final JButton play = new JButton(new AbstractAction("Play!") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveRoundAnswers(getAnswers());
			
			final ScenarioRound nextRound = menu.getLogic().getCurrentScenarioSession().getNextRound();
			if (nextRound == null) {
				log.info("no next round, game over");
				menu.show(GameOverScreen.class);
			} else {
				log.info("starting next round");
				menu.getLogic().start(nextRound);
			}
		}
	});

	public AfterRoundQuestionnaireScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	protected JButton nextButton() {
		return play;
	}

	@Override
	protected Questionnaire updateQuestionnaire(final GameLogic logic) {
		log.info("updating to after/scenarioround qn");
		return logic.getCurrentScenarioRound().getQuestionnaire();
	}
}
