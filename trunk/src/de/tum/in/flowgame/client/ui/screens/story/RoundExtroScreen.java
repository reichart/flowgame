package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.client.ui.screens.BeforeRoundQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.MenuScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;
import de.tum.in.flowgame.model.GameRound;

/**
 * Displayed after each gameplay-highscore-questionnaire block before the game
 * session ends or the next round starts.
 */
public class RoundExtroScreen extends MenuScreen {

	private final static Log log = LogFactory.getLog(RoundExtroScreen.class);

	private final JButton next = new JButton(new AbstractAction(UIMessages.CONTINUE) {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().initNextRound();
			
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

	public RoundExtroScreen() {
		super(Utils.imageResource("/res/spacestation.png", null));
	}

	@Override
	public Container getContents() {
		return centered(BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH),
				title(UIMessages.getString("extro")), next);
	}

}