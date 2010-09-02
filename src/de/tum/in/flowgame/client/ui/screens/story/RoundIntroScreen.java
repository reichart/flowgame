package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Story screen just before game play starts.
 */
public class RoundIntroScreen extends StoryScreen {

	private final static Log log = LogFactory.getLog(RoundIntroScreen.class);

	@Override
	protected JButton next() {
		return new JButton(new AbstractAction(UIMessages.getString("game.play")) {
			public void actionPerformed(final ActionEvent e) {
				log.info("starting current round");
				menu.getLogic().startCurrentRound();
			}
		});
	}

	@Override
	protected String getTitleKey() {
		return "game.action";
	}

	@Override
	protected String getTextKey() {
		final int storyScenario = menu.getLogic().getStoryScenario();
		final int storyPart = menu.getLogic().getStoryPart();
		String baseline = "";
		if (menu.getLogic().getCurrentScenarioRound().isBaselineRound()) {
			baseline = ".baseline";
		}
		return "scenario" + storyScenario + ".intro" + storyPart + baseline;
	}

}
