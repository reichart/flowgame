package de.tum.in.flowgame.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Difficulty;
import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.model.functions.ConstantFunction;
import de.tum.in.flowgame.model.functions.LinearFunction;

public class DDLGenerator {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDP");
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
		
		//Create 6 Players
		Person p0 = new Person(1071363107L);
		p0.setName("Barbara");
		Person p1 = new Person(226900023L);
		p1.setName("Blitz");
		Person p2 = new Person(250900007L);
		p2.setName("Phil");
		Person p3 = new Person(1526292045L);
		p3.setName("Dennis");
		Person p4 = new Person(100000237633531L);
		p4.setName("Christopher");
		Person p5 = new Person(1651586484L);
		p5.setName("Sevi");
		
		List<Person> players = new ArrayList<Person>();
		players.add(p0);
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		players.add(p5);
		
		Difficulty d = new Difficulty();

		Function intervalFunction = new ConstantFunction(80.0);
		Function ratioFunction = new ConstantFunction(0.3);
		LinearFunction speedFunction = new LinearFunction(30D,4.0d/600);

		DifficultyFunction df = new DifficultyFunction();
		df.setIntervald(intervalFunction);
		df.setRatio(ratioFunction);
		df.setSpeed(speedFunction);
		
		ScenarioRound sr1 = new ScenarioRound();
		sr1.setBaselineModifier(1);
		sr1.setDifficultyFunction(df);
		sr1.setExpectedPlaytime(2000L);
		sr1.setQuestionnaire(qn);
		sr1.setBaselineRound(true);
		
		ScenarioRound sr2 = new ScenarioRound();
		sr2.setBaselineModifier(5);
		sr2.setDifficultyFunction(df);
		sr2.setExpectedPlaytime(2000L);
		sr2.setQuestionnaire(qn);
		
		ScenarioSession ss = new ScenarioSession();
		ss.getRounds().add(sr1);
		ss.getRounds().add(sr2);
		
		//Create 1 GameSession for each player
		Random rnd = new Random();
		List<GameSession> gameSessions = new ArrayList<GameSession>();
		for (int i = 0; i < 6; i++) {
			GameSession gs = new GameSession();
			gs.setScenarioSession(ss);
			gs.setPlayer(players.get(i));
			
			//Create 4 GameRounds for each player
			for (int j = 0; j < 4; j++) {
				GameRound gr = new GameRound();
				gr.setScenarioRound(sr1);
				gr.setScore(Math.abs(rnd.nextLong()) % 10000);
				gs.addRound(gr);
			}
						
			gameSessions.add(gs);
		}
		
		em.getTransaction().begin();
		em.persist(q1);
		em.persist(qn);
		em.persist(ans);
		em.persist(p1);
		em.persist(p2);
		em.persist(p3);
		em.persist(p4);
		em.persist(p5);
		em.persist(d);
		em.persist(intervalFunction);
		em.persist(speedFunction);
		em.persist(df);
		em.persist(ss);
		for (GameSession gameSession : gameSessions) {
			em.persist(gameSession);
		}		
		em.getTransaction().commit();
	}
}