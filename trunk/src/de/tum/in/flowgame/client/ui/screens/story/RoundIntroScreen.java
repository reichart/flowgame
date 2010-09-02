package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Story screen just before game play starts.
 */
public class RoundIntroScreen extends StoryScreen {

	private final static Log log = LogFactory.getLog(RoundIntroScreen.class);

	private final JButton play = new JButton(new AbstractAction(UIMessages.getString("game.play")) {
		public void actionPerformed(final ActionEvent e) {
			log.info("starting current round");
			menu.getLogic().startCurrentRound();
		}
	});

	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
	}
	
	@Override
	public Container getContents() {
		return centered(COMMON_BORDER, title(UIMessages.getString("game.action")), text, play);
	}

	@Override
	protected String getText() {
		final int storyScenario = menu.getLogic().getStoryScenario();
		final int storyPart = menu.getLogic().getStoryPart();
		String baseline = "";
		if (menu.getLogic().getCurrentScenarioRound().isBaselineRound()) {
			baseline = ".baseline";
		}
		return UIMessages.getString("scenario" + storyScenario + ".intro" + storyPart + baseline);
	}

}
