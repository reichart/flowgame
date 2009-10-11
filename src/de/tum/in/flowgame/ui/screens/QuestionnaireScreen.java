package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.QuestionnairePanel;

public class QuestionnaireScreen extends MenuScreen {

	private final QuestionnairePanel qpanel;
	private Questionnaire q;
	private final JScrollPane qscrollpane;

	private final JButton play = new JButton(new AbstractAction("Play!") {

		@Override
		public void actionPerformed(final ActionEvent e) {
			qpanel.getAnswers(); // TODO put into GameRound
			menu.getLogic().start();
		}
	});

	public QuestionnaireScreen(final GameMenu menu) {
		super(menu);
		this.qpanel = new QuestionnairePanel();
		this.qscrollpane = new JScrollPane(this.qpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		update();
	}

	@Override
	public Container getContents() {
		return centered(title(q.getName()), qscrollpane, play);
	}

	@Override
	public void update() {
		q = menu.getLogic().getCurrentScenarioRound().getQuestionnaire();
		if (q == null) {
			// TODO this should never happen
			q = new Questionnaire();
			q.setName("dummy");
		}
		qpanel.setQuestionnaire(q);
	}

}
