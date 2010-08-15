package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTextArea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.client.ui.screens.MenuScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Story screen just before game play starts.
 */
public class RoundIntroScreen extends MenuScreen {

	private final static Log log = LogFactory.getLog(RoundIntroScreen.class);

	private final static JTextArea text = new JTextArea(UIMessages.getString("story.intro1"), 5, 100);

	private final JButton play = new JButton(new AbstractAction(UIMessages.getString("game.play")) {
		public void actionPerformed(final ActionEvent e) {
			log.info("starting current round");
			menu.getLogic().startCurrentRound();
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
		return centered(COMMON_BORDER, title(UIMessages.getString("game.action")), text, play);
	}

}
