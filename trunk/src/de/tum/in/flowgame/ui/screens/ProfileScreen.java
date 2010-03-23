package de.tum.in.flowgame.ui.screens;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.GameMenu;

/**
 * Empty menu to avoid artifacts when switching the menu back on after a game.
 * Without an empty menu, the previous screen could briefly be seen.
 */
public class ProfileScreen extends QuestionnaireScreen {

	private final JButton createProfile = new JButton(new AbstractAction("Play!") {
		public void actionPerformed(final ActionEvent e) {
			// TODO save answers of profiel questionnaire
			menu.show(MainScreen.class);
		}
	});
	private final Questionnaire qn;

	public ProfileScreen(final GameMenu menu) {
		super(menu);

		// TODO get this from server
		this.qn = new Questionnaire("Persönlichkeitsbeschreibung",
				"Die Folgenden eignen sich zur Beschreibung Ihrer eigenen Person (insgesamt 13 Aussagen). Lesen Sie bitte jede Aussage aufmerksam durch. Zur Beantwortung steht Ihnen eine kontinuierlich Skala von starker Ablehnung (d.h. die Beschreibung trifft überhaupt nicht auf Sie zu) bis zu einer starken Zustimmung (d.h. die Beschreibung trifft voll auf Sie zu) zur Verfügung."
				+ "Es gibt keine „richtigen“ oder „falschen“ Antworten. Sie bringen mit Ihren Antworten vielmehr Ihre persönliche Sichtweise zum Ausdruck. Wenn Ihnen einmal die Entscheidung schwer fallen sollte, geben Sie dann die Ausprägung an, die noch am ehesten auf Sie zutrifft.");
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
	}

	@Override
	protected JButton nextButton() {
		return createProfile;
	}

	@Override
	protected Questionnaire updateQuestionnaire(final GameLogic logic) {
		return qn;
	}
}
