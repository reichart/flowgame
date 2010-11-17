package de.tum.in.flowgame.server;

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
import de.tum.in.flowgame.model.functions.ConstantFunctionBaseline;
import de.tum.in.flowgame.model.functions.LinearFunction;
import de.tum.in.flowgame.model.functions.LinearFunctionBaseline;
import de.tum.in.flowgame.model.functions.LnFunction;
import de.tum.in.flowgame.model.functions.SigmoidBaselineFunction;

public class DDLGenerator {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDP");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// questionnaires used for player creation
		Questionnaire profile = new Questionnaire("profile", false, 13);
		
		// questionnaires used after every round
		Questionnaire flow = new Questionnaire("flow", false, 10);
		Questionnaire reqfit = new Questionnaire("reqfit", true, 1);

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
		Function speedFunction2 = new LinearFunction(60D, 0.015);
		
		Function speedFunction3 = new LnFunction(31.5, 6.5, 0.0);
		Function speedFunction4 = new LnFunction(25.55, 1.0, 60);
		
		Function speedFunction5 = new SigmoidBaselineFunction();
		
		DifficultyFunction df1 = new DifficultyFunction(intervalFunction, speedFunction, ratioFunction);
		DifficultyFunction df2 = new DifficultyFunction(intervalFunction, speedFunction2, ratioFunction);
		DifficultyFunction df3 = new DifficultyFunction(intervalFunction, speedFunction3, ratioFunction);
		DifficultyFunction df4 = new DifficultyFunction(intervalFunction, speedFunction4, ratioFunction);
		DifficultyFunction df5 = new DifficultyFunction(intervalFunction, speedFunction5, ratioFunction);

		final long timeLimit = 120*1000;
		
		//Create Scenario with Constant functions and previos baseline
//		Function speedFunctionConstant1 = new ConstantFunction(-60);
//		Function speedFunctionConstant2 = new ConstantFunction(-30);
//		Function speedFunctionConstant3 = new ConstantFunction(0);
//		Function speedFunctionConstant4 = new ConstantFunction(30);
//		Function speedFunctionConstant5 = new ConstantFunction(60);
//		
//		Function speedFunctionLinear1 = new LinearFunction(-75, 40f/timeLimit);
//		Function speedFunctionLinear2 = new LinearFunction(-45, 40f/timeLimit);
//		Function speedFunctionLinear3 = new LinearFunction(-15, 40f/timeLimit);
//		Function speedFunctionLinear4 = new LinearFunction(15, 40f/timeLimit);
//		Function speedFunctionLinear5 = new LinearFunction(45, 40f/timeLimit);
		
		Function speedFunctionConstant1 = new ConstantFunctionBaseline(-30, 0.85);
//		Function speedFunctionConstant2 = new ConstantFunctionBaseline(-15, 0.925);
		Function speedFunctionConstant3 = new ConstantFunctionBaseline(0, 1);
//		Function speedFunctionConstant4 = new ConstantFunctionBaseline(15, 1.075);
		Function speedFunctionConstant5 = new ConstantFunctionBaseline(30, 1.15);
		
		Function speedFunctionLinear1 = new LinearFunctionBaseline(-30, 0.85);
//		Function speedFunctionLinear2 = new LinearFunctionBaseline(-15, 0.925);
		Function speedFunctionLinear3 = new LinearFunctionBaseline(0, 1);
//		Function speedFunctionLinear4 = new LinearFunctionBaseline(15, 1.075);
		Function speedFunctionLinear5 = new LinearFunctionBaseline(30, 1.15);

		ScenarioRound cBaselineRound = new ScenarioRound(true, timeLimit, df5);
		ScenarioRound cSr1 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant1, ratioFunction));
//		ScenarioRound cSr2 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant2, ratioFunction));
		ScenarioRound cSr3 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant3, ratioFunction));
//		ScenarioRound cSr4 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant4, ratioFunction));
		ScenarioRound cSr5 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionConstant5, ratioFunction));
		
		ScenarioSession constantScenarioSession = new ScenarioSession();
		constantScenarioSession.add(cBaselineRound);
		constantScenarioSession.add(cSr1);
//		constantScenarioSession.add(cSr2);
		constantScenarioSession.add(cSr3);
//		constantScenarioSession.add(cSr4);
		constantScenarioSession.add(cSr5);
		em.persist(constantScenarioSession);
		
		
		ScenarioRound dBaselineRound = new ScenarioRound(true, timeLimit, df5);
		ScenarioRound dSr1 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionLinear1, ratioFunction));
//		ScenarioRound dSr2 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionLinear2, ratioFunction));
		ScenarioRound dSr3 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionLinear3, ratioFunction));
//		ScenarioRound dSr4 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionLinear4, ratioFunction));
		ScenarioRound dSr5 = new ScenarioRound(false, timeLimit, new DifficultyFunction(intervalFunction, speedFunctionLinear5, ratioFunction));
		
		ScenarioSession linearScenarioSession = new ScenarioSession();
		linearScenarioSession.add(dBaselineRound);
		linearScenarioSession.add(dSr1);
//		linearScenarioSession.add(dSr2);
		linearScenarioSession.add(dSr3);
//		linearScenarioSession.add(dSr4);
		linearScenarioSession.add(dSr5);
		em.persist(linearScenarioSession);
		
		
		//Create 1 GameSession for each player
		
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
		
		em.persist(flow);
		em.persist(reqfit);
		em.persist(profile);
		
		em.getTransaction().commit();
	}
}