package de.tum.in.flowgame.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.tum.in.flowgame.model.Difficulty;
import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.model.functions.ConstantFunction;
import de.tum.in.flowgame.model.functions.LinearFunction;
import de.tum.in.flowgame.model.functions.LnFunction;

public class DDLGenerator {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDP");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// once when user first plays game
		Questionnaire profile = new Questionnaire("Persönlichkeitsbeschreibung", "Die Folgenden eignen sich zur Beschreibung Ihrer eigenen Person (insgesamt 13 Aussagen). Lesen Sie bitte jede Aussage aufmerksam durch. Zur Beantwortung steht Ihnen eine kontinuierlich Skala von starker Ablehnung (d.h. die Beschreibung trifft überhaupt nicht auf Sie zu) bis zu einer starken Zustimmung (d.h. die Beschreibung trifft voll auf Sie zu) zur Verfügung."
						+ "Es gibt keine „richtigen“ oder „falschen“ Antworten. Sie bringen mit Ihren Antworten vielmehr Ihre persönliche Sichtweise zum Ausdruck. Wenn Ihnen einmal die Entscheidung schwer fallen sollte, geben Sie dann die Ausprägung an, die noch am ehesten auf Sie zutrifft.");
		profile.addQuestion("Viel Leute halten mich für etwas kühl und distanziert.");
		profile.addQuestion("Probleme, die schwierig zu lösen sind, reizen mich.");
		profile.addQuestion("Ich bin ein fröhlicher, gut gelaunter Mensch.");
		profile.addQuestion("Zu häufig bin ich entmutigt und will aufgeben, wenn etwas schief geht.");
		profile.addQuestion("Ich strebe danach, alles mir Mögliche zu erreichen.");
		profile.addQuestion("Ich bin häufig beunruhigt, über Dinge, die schief gehen könnten.");
		profile.addQuestion("Ich fühle mich besonders erfolgreich, wenn ich eine neue Idee darüber bekommen habe, wie eine Sache funktioniert.");
		profile.addQuestion("Ich arbeite zielstrebig und effektiv.");
		profile.addQuestion("Ich ziehe es gewöhnlich vor, Dinge allein zu tun.");
		profile.addQuestion("Mich reizen Situationen, in denen ich meine Fähigkeiten testen kann.");
		profile.addQuestion("Ich fühle mich oft hilflos und wünsche mir eine Person, die meine Probleme löst.");
		profile.addQuestion("Ich habe gerne viele Leute um mich herum.");
		profile.addQuestion("Ich fühle mich besonders erfolgreich, wenn ich eine wirklich komplizierte Sache endgültig verstanden habe.");
		
		// once before each session
		Questionnaire moodAndSkills = new Questionnaire("Stimmung",
				"Die Folgenden sollen feststellen, wie Sie sich gerade fühlen. Lesen Sie bitte jede Aussage aufmerksam durch. Zur Beantwortung steht Ihnen eine kontinuierlich Skala von starker Ablehnung (d.h. die Beschreibung trifft überhaupt nicht auf Sie zu) bis zu einer starken Zustimmung (d.h. die Beschreibung trifft voll auf Sie zu) zur Verfügung."
				+ "Es gibt keine „richtigen“ oder „falschen“ Antworten. Sie bringen mit Ihren Antworten vielmehr Ihre persönliche Sichtweise zum Ausdruck. Wenn Ihnen einmal die Entscheidung schwer fallen sollte, geben Sie dann die Ausprägung an, die noch am ehesten auf Sie zutrifft.");
		moodAndSkills.addQuestion("zufrieden <-> unzufrieden");
		moodAndSkills.addQuestion("energiegeladen <-> energielos");
		moodAndSkills.addQuestion("gestresst <-> entspannt");
		moodAndSkills.addQuestion("müde <-> hellwach");
		moodAndSkills.addQuestion("friedlich <-> verärgert");
		moodAndSkills.addQuestion("unglücklich <-> glücklich");
		moodAndSkills.addQuestion("lustlos <-> hoch motiviert");
		moodAndSkills.addQuestion("ruhig <-> nervös");
		moodAndSkills.addQuestion("begeistert <-> gelangweilt");
		moodAndSkills.addQuestion("besorgt <-> sorgenfrei");
		
		// after every round
		Questionnaire howWasIt = new Questionnaire("How was it?", "TBD mood (15q) and flow (20q)");
		howWasIt.addQuestion("how was it question");
		
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
		
		for (final Person player : players) {
			em.persist(player);
		}
		
		Difficulty d = new Difficulty(0, 0, 0);

		Function intervalFunction = new ConstantFunction(80.0);
		Function ratioFunction = new ConstantFunction(0.3);
		
		//useful functions for baseline measurment
		Function speedFunction = new LinearFunction(60D,0.02);
		Function speedFunction2 = new LnFunction(31.5, 6.5, 0.0);
		Function speedFunction3 = new LnFunction(25.55, 1.0, 60);
		Function speedFunction4 = new LinearFunction(60D, 0.015);
		
		DifficultyFunction df1 = new DifficultyFunction(intervalFunction, speedFunction, ratioFunction);
		DifficultyFunction df2 = new DifficultyFunction(intervalFunction, speedFunction2, ratioFunction);
		DifficultyFunction df3 = new DifficultyFunction(intervalFunction, speedFunction3, ratioFunction);
		DifficultyFunction df4 = new DifficultyFunction(intervalFunction, speedFunction4, ratioFunction);
		
		//Create Scenario with Constant functions and previos baseline
		Function speedFunctionConstant1 = new ConstantFunction(-60);
		Function speedFunctionConstant2 = new ConstantFunction(-30);
		Function speedFunctionConstant3 = new ConstantFunction(0);
		Function speedFunctionConstant4 = new ConstantFunction(30);
		Function speedFunctionConstant5 = new ConstantFunction(60);
		
		ScenarioRound cBaselineRound = new ScenarioRound(true, 0, null, df2, howWasIt);
		ScenarioRound cSr1 = new ScenarioRound(false, 0, null, new DifficultyFunction(intervalFunction, speedFunctionConstant1, ratioFunction), howWasIt);
		ScenarioRound cSr2 = new ScenarioRound(false, 0, null, new DifficultyFunction(intervalFunction, speedFunctionConstant2, ratioFunction), howWasIt);
		ScenarioRound cSr3 = new ScenarioRound(false, 0, null, new DifficultyFunction(intervalFunction, speedFunctionConstant3, ratioFunction), howWasIt);
		ScenarioRound cSr4 = new ScenarioRound(false, 0, null, new DifficultyFunction(intervalFunction, speedFunctionConstant4, ratioFunction), howWasIt);
		ScenarioRound cSr5 = new ScenarioRound(false, 0, null, new DifficultyFunction(intervalFunction, speedFunctionConstant5, ratioFunction), howWasIt);
		
		ScenarioSession constantScenarioSession = new ScenarioSession(ScenarioSession.Type.SOCIAL, moodAndSkills);
		constantScenarioSession.add(cBaselineRound);
		constantScenarioSession.add(cSr1);
		constantScenarioSession.add(cSr2);
		constantScenarioSession.add(cSr3);
		constantScenarioSession.add(cSr4);
		constantScenarioSession.add(cSr5);
		em.persist(constantScenarioSession);
		
		ScenarioRound sr1 = new ScenarioRound(true, 1, 2000L, df1, howWasIt);
		ScenarioRound sr2 = new ScenarioRound(true, 1, 2000L, df2, howWasIt);
		ScenarioRound sr3 = new ScenarioRound(true, 1, 2000L, df3, howWasIt);
		ScenarioRound sr4 = new ScenarioRound(true, 1, 2000L, df4, howWasIt);
		
//		ScenarioRound sr5 = new ScenarioRound(false, 0, null, df5, howWasIt);
//		ScenarioRound sr6 = new ScenarioRound(false, 0, null, df5, howWasIt);
//		ScenarioRound sr7 = new ScenarioRound(false, 0, null, df1, howWasIt);
		
//		ScenarioSession ss = new ScenarioSession(null, moodAndSkills);
//		ss.add(sr1);
//		ss.add(sr2);
//		ss.add(sr3);
//		ss.add(sr4);
//		ss.add(sr5);
//		ss.add(sr6);
//		ss.add(sr7);
		
		//Create 1 GameSession for each player
		em.persist(constantScenarioSession);
		
		final Random rnd = new Random(0xCAFEBABE);
		for (Person player : players) {
			GameSession gs = new GameSession(player.getId(), constantScenarioSession);
			
			//Create 4 GameRounds for each player
			for (int j = 0; j < 4; j++) {
				final GameRound gr = new GameRound(cSr1);
				// spread start times so score charts 
				gr.setStartTime(System.currentTimeMillis() + 60*j);
				gr.setScore(Math.abs(rnd.nextLong()) % 10000);
				gs.getRounds().add(gr);
			}
			em.merge(gs);
		}
		
		em.persist(d);
		em.persist(intervalFunction);
		em.persist(df2);
		em.persist(howWasIt);
		
		em.getTransaction().commit();
	}
}