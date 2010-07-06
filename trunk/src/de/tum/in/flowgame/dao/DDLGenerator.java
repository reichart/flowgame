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
		
		//create questionnaires
		// questionnaire that is used for player creation
		Questionnaire profile = new Questionnaire("profile", false, 13);
		// questionnaires that are used once before each session
		Questionnaire moodAndSkills = new Questionnaire("mood",true, 10);
		Questionnaire moodAndSkillsShort = new Questionnaire("moodShort", true, 3);
		// questionnaires that are used  after every round
		Questionnaire flow = new Questionnaire("flow", false, 10);

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
				
		ScenarioRound cBaselineRound = new ScenarioRound(true, 0, timeLimit, df2);
		ScenarioRound cSr1 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant1, ratioFunction));
		ScenarioRound cSr2 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant2, ratioFunction));
		ScenarioRound cSr3 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant3, ratioFunction));
		ScenarioRound cSr4 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant4, ratioFunction));
		ScenarioRound cSr5 = new ScenarioRound(false, 0, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant5, ratioFunction));
		
		ScenarioSession constantScenarioSession = new ScenarioSession(ScenarioSession.Type.SOCIAL);
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
		
		em.persist(moodAndSkills);
		em.persist(moodAndSkillsShort);
		em.persist(flow);
		em.persist(profile);
		
		em.getTransaction().commit();
	}
}