package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.QuestionnairePanel;

public class QuestionnaireScreen extends MenuScreen {

	private final QuestionnairePanel qpanel;
	private final Questionnaire q;

	private final JButton play = new JButton(new AbstractAction("Play!") {

		@Override
		public void actionPerformed(final ActionEvent e) {
			qpanel.getAnswers(); // TODO put into GameRound
			menu.getLogic().start();
		}
	});

	public QuestionnaireScreen(final GameMenu menu) {
		super(menu);

		// TODO load Questionnaire from server
//		final DownloadScenarioSession dss = new DownloadScenarioSession();
//		final ScenarioSession session = dss.download(new Person(0xCAFEBABEL));
//		final List<ScenarioRound> rounds = session.getRounds();
//		this.q = rounds.get(0).getQuestionnaire();
		
		this.q = new Questionnaire();
		q.setName("dummy questionnaire");

		this.qpanel = new QuestionnairePanel(q);
	}

	@Override
	public Container getContents() {
		return centered(title(q.getName()), qpanel, play);
	}
}
