package de.tum.in.flowgame.ui.screens;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.QuestionnairePanel;

/**
 * Empty menu to avoid artifacts when switching the menu back on after a game.
 * Without an empty menu, the previous screen could briefly be seen.
 */
public class ProfileScreen extends MenuScreen {

	private final JButton okay = goTo("Create profile", MainScreen.class);

	private QuestionnairePanel qpanel;

	private JScrollPane qscrollpane;
	
	public ProfileScreen(final GameMenu menu) {
		super(menu);
		this.qpanel = new QuestionnairePanel();
		
		Question q1 = new Question();
		q1.setText("Viel Leute halten mich für etwas kühl und distanziert.");
		Question q2 = new Question();
		q2.setText("Probleme, die schwierig zu lösen sind, reizen mich.");
		Question q3 = new Question();
		q3.setText("Ich bin ein fröhlicher, gut gelaunter Mensch.");
		Question q4 = new Question();
		q4.setText("Zu häufig bin ich entmutigt und will aufgeben, wenn etwas schief geht.");
		Question q5 = new Question();
		q5.setText("Ich strebe danach, alles mir Mögliche zu erreichen.");
		Question q6 = new Question();
		q6.setText("Ich bin häufig beunruhigt, über Dinge, die schief gehen könnten.");
		Question q7 = new Question();
		q7.setText("Ich fühle mich besonders erfolgreich, wenn ich eine neue Idee darüber bekommen habe, wie eine Sache funktioniert.");
		Question q8 = new Question();
		q8.setText("Ich arbeite zielstrebig und effektiv.");
		Question q9 = new Question();
		q9.setText("Ich ziehe es gewöhnlich vor, Dinge allein zu tun.");
		Question q10 = new Question();
		q10.setText("Mich reizen Situationen, in denen ich meine Fähigkeiten testen kann.");
		Question q11 = new Question();
		q11.setText("Ich fühle mich oft hilflos und wünsche mir eine Person, die meine Probleme löst.");
		Question q12 = new Question();
		q12.setText("Ich habe gerne viele Leute um mich herum.");
		Question q13 = new Question();
		q13.setText("Ich fühle mich besonders erfolgreich, wenn ich eine wirklich komplizierte Sache endgültig verstanden habe.");
		Questionnaire qn = new Questionnaire();
		qn.setName("Persönlichkeitsbeschreibung");
		qn.setDescription("Die Folgenden eignen sich zur Beschreibung Ihrer eigenen Person (insgesamt 13 Aussagen). Lesen Sie bitte jede Aussage aufmerksam durch. Zur Beantwortung steht Ihnen eine kontinuierlich Skala von starker Ablehnung (d.h. die Beschreibung trifft überhaupt nicht auf Sie zu) bis zu einer starken Zustimmung (d.h. die Beschreibung trifft voll auf Sie zu) zur Verfügung." +
			"Es gibt keine „richtigen“ oder „falschen“ Antworten. Sie bringen mit Ihren Antworten vielmehr Ihre persönliche Sichtweise zum Ausdruck. Wenn Ihnen einmal die Entscheidung schwer fallen sollte, geben Sie dann die Ausprägung an, die noch am ehesten auf Sie zutrifft.");
		qn.addQuestion(q1);
		qn.addQuestion(q2);
		qn.addQuestion(q3);
		qn.addQuestion(q4);
		qn.addQuestion(q5);
		qn.addQuestion(q6);
		qn.addQuestion(q7);
		qn.addQuestion(q8);
		qn.addQuestion(q9);
		qn.addQuestion(q10);
		qn.addQuestion(q11);
		qn.addQuestion(q12);
		qn.addQuestion(q13);
		
		qpanel.setQuestionnaire(qn);
		
		this.qscrollpane = new JScrollPane(this.qpanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	@Override
	public Container getContents() {
		return centered(title("profile"), qscrollpane, okay);
	}

}
