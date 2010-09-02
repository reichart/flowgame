package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.BeforeRoundQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;
import de.tum.in.flowgame.model.GameRound;

/**
 * Displayed after each gameplay-highscore-questionnaire block before the game
 * session ends or the next round starts.
 */
public class RoundExtroScreen extends StoryScreen {

	private final static Log log = LogFactory.getLog(RoundExtroScreen.class);

	private final JButton next = new JButton(new AbstractAction(UIMessages.CONTINUE) {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().initNextRound();
			
			menu.getLogic().nextStoryPart();
			
			final GameRound round = menu.getLogic().getCurrentGameRound();
			if (round == null) {
				log.info("no more rounds, game over");
				menu.show(SessionExtroScreen.class);
			} else {
				log.info("more rounds available, showing pre-round qn: " + round);
				menu.show(BeforeRoundQuestionnaireScreen.class);
			}
		}
	});
	
	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
	}

	@Override
	public Container getContents() {
		return centered(COMMON_BORDER, title(UIMessages.getString("extro")), text, next);
	}

	@Override
	protected String getText() {
		final GameLogic logic = menu.getLogic();
		final int storyScenario = logic .getStoryScenario();
		final int storyPart = logic.getStoryPart();
		String baseline = "";
		if (menu.getLogic().getCurrentScenarioRound().isBaselineRound()) {
			baseline = ".baseline";
		}
		return UIMessages.getString("scenario" + storyScenario + ".extro" + storyPart + baseline);
	}

}