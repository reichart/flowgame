package de.tum.in.flowgame.client.ui.screens;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.story.RoundIntroScreen;

/**
 * Displays the long mood questionnaire at the beginning of a game session.
 */
public class BeforeSessionQuestionnaireScreen extends QuestionnaireScreen {

	private final static Log log = LogFactory.getLog(BeforeSessionQuestionnaireScreen.class);

	private final Action next = new AbstractAction(UIMessages.CONTINUE) {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().saveSessionAnswers(getAnswers());
			menu.getLogic().initNextRound();
			menu.show(RoundIntroScreen.class);
		}
	};

	@Override
	protected Action next() {
		return next;
	}

	@Override
	public final void update(final GameLogic logic) throws Exception {
		super.update(logic);
		log.info("creating new session, updating to before/scenariosession qn");
		logic.newSession();
	}

	@Override
	protected List<String> getQuestionnaireNames() {
		return Arrays.asList("mood");
	}
}
