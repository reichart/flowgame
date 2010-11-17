package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.UIMessages;
import de.tum.in.flowgame.model.GameRound;

/**
 * Displayed after each gameplay-highscore-questionnaire block before the game
 * session ends or the next round starts.
 */
public class RoundExtroScreen extends StoryScreen {

	private final static Log log = LogFactory.getLog(RoundExtroScreen.class);

	@Override
	protected JButton next() {
		return new JButton(new AbstractAction(UIMessages.CONTINUE) {
			public void actionPerformed(final ActionEvent e) {
				menu.getLogic().initNextRound();

				menu.getLogic().nextStoryPart();

				final GameRound round = menu.getLogic().getCurrentGameRound();
				if (round == null) {
					log.info("no more rounds, game over");
					menu.show(SessionExtroScreen.class);
				} else {
					log.info("more rounds available, showing round-intro: " + round);
					menu.show(RoundIntroScreen.class);
				}
			}
		});
	}

	@Override
	protected String getTitleKey() {
		return "extro";
	}

	@Override
	protected String getTextKey() {
		final GameLogic logic = menu.getLogic();
		final int storyScenario = logic.getStoryScenario();
		final int storyPart = logic.getStoryPart();
		String baseline = "";
		if (menu.getLogic().getCurrentScenarioRound().isBaselineRound()) {
			baseline = ".baseline";
		}
		return "scenario" + storyScenario + ".extro" + storyPart + baseline;
	}

}