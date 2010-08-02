package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextArea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.ui.screens.MenuScreen;

/**
 * Story screen just before game play starts.
 */
public class RoundIntroScreen extends MenuScreen {

	private final static Log log = LogFactory.getLog(RoundIntroScreen.class);

	private final static JTextArea text = new JTextArea("There has been a power failure in one of our outer-rim antimatter processing plant. " +
			"Unfortunately, the base is not equipped to deal with this kind of incident. Your mission is to bring a team of engineers there to " +
			"repair the damage. Remember to avoid the anomalies in hyperspace at all cost and to collect as much of the valuable Crystalis as " +
			"possible.", 5, 100);

	private final JButton play = new JButton(new AbstractAction("Play!") {
		public void actionPerformed(final ActionEvent e) {
			log.info("starting first round");
			final ScenarioSession scenarioSession = menu.getLogic().getCurrentScenarioSession();
			menu.getLogic().start(scenarioSession.getNextRound(true));
		}
	});

	public RoundIntroScreen() {
		super(Utils.imageResource("/res/spacestation.png", null));
	}

	@Override
	public Container getContents() {
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		return centered(COMMON_BORDER, title("Prepare for Action"), text, play);
	}

}
