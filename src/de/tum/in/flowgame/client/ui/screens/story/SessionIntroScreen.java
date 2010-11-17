package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed when going from the main menu to the initial session questionnaire.
 */
public class SessionIntroScreen extends StoryScreen {

	private final static Log log = LogFactory.getLog(SessionIntroScreen.class);
	
	private final JButton next = new JButton(new AbstractAction(UIMessages.CONTINUE) {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().initNextRound();
			menu.show(RoundIntroScreen.class);
		}
	});
	
	@Override
	protected JButton next() {
		return next;
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
		
		log.info("creating new session, updating to before/scenariosession qn");
		logic.newSession();
	}
	
	@Override
	protected String getTitleKey() {
		return "scenario" + menu.getLogic().getStoryScenario() + ".name";
	}

	@Override
	protected String getTextKey() {
		return "scenario" + menu.getLogic().getStoryScenario() + ".intro";
	}

}