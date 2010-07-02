package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.ui.screens.SettingIconsScreen;

/**
 * Displayed after each gameplay-highscore-questionnaire block before the game
 * session ends or the next round starts.
 */
public class RoundExtroScreen extends SettingIconsScreen {

	private final static Log log = LogFactory.getLog(RoundExtroScreen.class);

	private final JButton next = new JButton(new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			final ScenarioRound nextRound = menu.getLogic().getCurrentScenarioSession().getNextRound(false);
			if (nextRound == null) {
				log.info("no next round, game over");
				menu.show(SessionExtroScreen.class);
			} else {
				log.info("preparing for next round, back to intro screen");
				menu.show(RoundIntroScreen.class);
			}
		}
	});

	public RoundExtroScreen() {
		super(Utils.imageResource("/res/spacestation.png", null));
	}

	@Override
	public Container getContents() {
		return centered(BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH),
				title("Game Session Extro"), next);
	}

}