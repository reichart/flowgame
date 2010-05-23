package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.ui.GameMenu;

/**
 * Story screen just before game play starts.
 */
public class GameSessionIntroScreen extends MenuScreen {

	private final static Log log = LogFactory.getLog(GameSessionIntroScreen.class);

	private final static JLabel image = new JLabel(new ImageIcon(Utils.imageResource("/res/spacestation.png", null)));

	private final JButton play = new JButton(new AbstractAction("Play!") {
		public void actionPerformed(final ActionEvent e) {
			log.info("starting first round");
			final ScenarioSession scenarioSession = menu.getLogic().getCurrentScenarioSession();
			menu.getLogic().start(scenarioSession.getNextRound(true));
		}
	});

	public GameSessionIntroScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return centered(title("Prepare for Action"), image, play);
	}

}
