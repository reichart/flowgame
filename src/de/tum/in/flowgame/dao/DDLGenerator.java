package de.tum.in.flowgame.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mysql.jdbc.Driver;

import de.tum.in.flowgame.functions.ConstantFunction;
import de.tum.in.flowgame.functions.LinearFunction;
import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Difficulty;
import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;

public class DDLGenerator {

	public static void main(String[] args) {
		
		Driver.class.getName();
		
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("IDP");
			EntityManager em = emf.createEntityManager();

			
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
			
			Answer ans = new Answer(q1, 1);
			Person p = new Person(12345L);
			p.setName("Ballala");
			Difficulty d = new Difficulty();

			Function intervalFunction = new ConstantFunction(80.0);
			Function ratioFunction = new ConstantFunction(0.6);
			LinearFunction speedFunction = new LinearFunction(30D,4.0d/600);

			DifficultyFunction df = new DifficultyFunction();
			df.setIntervald(intervalFunction);
			df.setRatio(ratioFunction);
			df.setSpeed(speedFunction);
			
			ScenarioRound sr = new ScenarioRound();
			sr.setBaselineModifier(1);
			sr.setDifficultyFunction(df);
			sr.setExpectedPlaytime(2000L);
			sr.setQuestionnaire(qn);
			
			ScenarioRound sr2 = new ScenarioRound();
			sr2.setBaselineModifier(5);
			sr2.setDifficultyFunction(df);
			sr2.setExpectedPlaytime(2000L);
			sr2.setQuestionnaire(qn);
			
			ScenarioSession ss = new ScenarioSession();
			ss.getRounds().add(sr);
			ss.getRounds().add(sr2);

			em.getTransaction().begin();
			em.persist(q1);
			em.persist(qn);
			em.persist(ans);
			em.persist(p);
			em.persist(d);
			em.persist(intervalFunction);
			em.persist(speedFunction);
			em.persist(df);
			em.persist(ss);
			em.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}