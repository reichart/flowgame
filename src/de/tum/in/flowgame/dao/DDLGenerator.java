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
		
//		Answer ans = new Answer(q1, 1);
		
		//Create 6 Players
		Person p0 = new Person(1071363107L, "Barbara");
		Person p1 = new Person(226900023L, "Blitz");
		Person p2 = new Person(250900007L, "Phil");
		Person p3 = new Person(1526292045L, "Dennis");
		Person p4 = new Person(100000237633531L, "Christopher");
		Person p5 = new Person(1651586484L, "Sevi");
		
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

		DifficultyFunction df = new DifficultyFunction(intervalFunction, speedFunction, ratioFunction);
		
		ScenarioRound sr1 = new ScenarioRound(true, 1, 2000L, df, qn);
		ScenarioRound sr2 = new ScenarioRound(false, 5, 2000L, df, qn);
		
		ScenarioSession ss = new ScenarioSession();
		ss.add(sr1);
		ss.add(sr2);
		
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
//		em.persist(q1);
		em.persist(qn);
//		em.persist(ans);
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