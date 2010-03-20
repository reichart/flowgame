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
		
		// TODO get questionnaire from server
		Questionnaire qn = new Questionnaire("Persönlichkeitsbeschreibung", "Die Folgenden eignen sich zur Beschreibung Ihrer eigenen Person (insgesamt 13 Aussagen). Lesen Sie bitte jede Aussage aufmerksam durch. Zur Beantwortung steht Ihnen eine kontinuierlich Skala von starker Ablehnung (d.h. die Beschreibung trifft überhaupt nicht auf Sie zu) bis zu einer starken Zustimmung (d.h. die Beschreibung trifft voll auf Sie zu) zur Verfügung." +
		"Es gibt keine „richtigen“ oder „falschen“ Antworten. Sie bringen mit Ihren Antworten vielmehr Ihre persönliche Sichtweise zum Ausdruck. Wenn Ihnen einmal die Entscheidung schwer fallen sollte, geben Sie dann die Ausprägung an, die noch am ehesten auf Sie zutrifft.");
		qn.addQuestion("Viel Leute halten mich für etwas kühl und distanziert.");
		qn.addQuestion("Probleme, die schwierig zu lösen sind, reizen mich.");
		qn.addQuestion("Ich bin ein fröhlicher, gut gelaunter Mensch.");
		qn.addQuestion("Zu häufig bin ich entmutigt und will aufgeben, wenn etwas schief geht.");
		qn.addQuestion("Ich strebe danach, alles mir Mögliche zu erreichen.");
		qn.addQuestion("Ich bin häufig beunruhigt, über Dinge, die schief gehen könnten.");
		qn.addQuestion("Ich fühle mich besonders erfolgreich, wenn ich eine neue Idee darüber bekommen habe, wie eine Sache funktioniert.");
		qn.addQuestion("Ich arbeite zielstrebig und effektiv.");
		qn.addQuestion("Ich ziehe es gewöhnlich vor, Dinge allein zu tun.");
		qn.addQuestion("Mich reizen Situationen, in denen ich meine Fähigkeiten testen kann.");
		qn.addQuestion("Ich fühle mich oft hilflos und wünsche mir eine Person, die meine Probleme löst.");
		qn.addQuestion("Ich habe gerne viele Leute um mich herum.");
		qn.addQuestion("Ich fühle mich besonders erfolgreich, wenn ich eine wirklich komplizierte Sache endgültig verstanden habe.");
		
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
