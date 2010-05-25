package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.ScenarioRound;

/**
 * Displayed after each gameplay-highscore-questionnaire block before the game
 * session ends or the next round starts.
 */
public class GameSessionExtroScreen extends MenuScreen {

	private final static Log log = LogFactory.getLog(GameSessionExtroScreen.class);

	private final JButton next = new JButton(new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			final ScenarioRound nextRound = menu.getLogic().getCurrentScenarioSession().getNextRound(false);
			if (nextRound == null) {
				log.info("no next round, game over");
				menu.show(GameOverScreen.class);
			} else {
				log.info("preparing for next round, back to intro screen");
				menu.show(GameSessionIntroScreen.class);
			}
		}
	});

	public GameSessionExtroScreen() {
		super(Utils.imageResource("/res/spacestation.png", null), BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));
	}

	@Override
	public Container getContents() {
		return centered(title("Game Session Extro"), next);
	}

}