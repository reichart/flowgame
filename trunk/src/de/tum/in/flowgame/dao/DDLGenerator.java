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
import de.tum.in.flowgame.model.functions.LnFunction;

public class DDLGenerator {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDP");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// once before each session
		Questionnaire moodAndSkills = new Questionnaire("Stimmung",
				"Bitte geben bei den unten stehenden Adjektivpaaren an, wie Sie sich im Moment fühlen.", true);
		moodAndSkills.addLabelQuestion("zufrieden", "unzufrieden");
		moodAndSkills.addLabelQuestion("energiegeladen", "energielos");
		moodAndSkills.addLabelQuestion("gestresst", "entspannt");
		moodAndSkills.addLabelQuestion("müde", "hellwach");
		moodAndSkills.addLabelQuestion("friedlich", "verärgert");
		moodAndSkills.addLabelQuestion("unglücklich", "glücklich");
		moodAndSkills.addLabelQuestion("lustlos", "hoch motiviert");
		moodAndSkills.addLabelQuestion("ruhig", "nervös");
		moodAndSkills.addLabelQuestion("begeistert", "gelangweilt");
		moodAndSkills.addLabelQuestion("besorgt", "sorgenfrei");
		
		Questionnaire moodAndSkillsShort = new Questionnaire("Stimmung", "", true);
		moodAndSkillsShort.addLabelQuestion("ruhig", "nervös");
		moodAndSkillsShort.addLabelQuestion("zufrieden","unzufrieden");
		moodAndSkillsShort.addLabelQuestion("lustlos", "hoch motiviert"); 
		
		// after every round
		Questionnaire howWasIt = new Questionnaire("How was it?", "Bitte versetzen Sie sich in die gerade beendete Spielrunde und beantworten Sie die Aussagen zur Erleben beim Spielen:");
		howWasIt.addQuestion("Ich fühle mich optimal beansprucht.");
		howWasIt.addQuestion("Meine Gedanken bzw. Aktivitäten laufen flüssig und glatt.");
		howWasIt.addQuestion("Ich merke gar nicht, wie die Zeit vergeht.");
		howWasIt.addQuestion("Ich habe keine Mühe, mich zu konzentrieren.");
		howWasIt.addQuestion("Mein Kopf ist völlig klar.");
		howWasIt.addQuestion("Ich bin ganz vertieft in das was ich gerade mache.");
		howWasIt.addQuestion("Die richtigen Gedanken/Bewegungen kommen wie von selbst.");
		howWasIt.addQuestion("Ich weiss bei jedem Schritt, was ich zu tun habe.");
		howWasIt.addQuestion("Ich habe das Gefühl, den Ablauf unter Kontrolle zu haben.");
		howWasIt.addQuestion("Ich bin völlig selbstvergessen.");
		
		howWasIt.addQuestion("Für mich persönlich sind die jetzigen Anforderungen...");//zu gering <-> zu hoch
		
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
		Function speedFunction2 = new LnFunction(31.5, 6.5, 0.0);
		DifficultyFunction df2 = new DifficultyFunction(intervalFunction, speedFunction2, ratioFunction);
		
		//Create Scenario with Constant functions and previos baseline
		Function speedFunctionConstant1 = new ConstantFunction(-60);
		Function speedFunctionConstant2 = new ConstantFunction(-30);
		Function speedFunctionConstant3 = new ConstantFunction(0);
		Function speedFunctionConstant4 = new ConstantFunction(30);
		Function speedFunctionConstant5 = new ConstantFunction(60);
		
		final long timeLimit = 60*1000;
		
		ScenarioRound cBaselineRound = new ScenarioRound(true, 0, timeLimit, df2, howWasIt);
		ScenarioRound cSr1 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant1, ratioFunction), howWasIt);
		ScenarioRound cSr2 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant2, ratioFunction), howWasIt);
		ScenarioRound cSr3 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant3, ratioFunction), howWasIt);
		ScenarioRound cSr4 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant4, ratioFunction), howWasIt);
		ScenarioRound cSr5 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant5, ratioFunction), howWasIt);
		
		ScenarioSession constantScenarioSession = new ScenarioSession(ScenarioSession.Type.SOCIAL, moodAndSkills);
		constantScenarioSession.add(cBaselineRound);
		constantScenarioSession.add(cSr1);
		constantScenarioSession.add(cSr2);
		constantScenarioSession.add(cSr3);
		constantScenarioSession.add(cSr4);
		constantScenarioSession.add(cSr5);
		em.persist(constantScenarioSession);
		
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