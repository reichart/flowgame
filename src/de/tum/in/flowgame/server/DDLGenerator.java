package de.tum.in.flowgame.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.model.functions.ConstantFunction;
import de.tum.in.flowgame.model.functions.ConstantFunctionBaseline;
import de.tum.in.flowgame.model.functions.LinearFunctionBaseline;
import de.tum.in.flowgame.model.functions.SigmoidBaselineFunction;

public class DDLGenerator {

	public static void main(final String[] args) {
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDP");
		final EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// questionnaires
		em.persist(new Questionnaire("flow", false, 4));
		em.persist(new Questionnaire("reqfit", true, 1));
		em.persist(new Questionnaire("profile", false, 13));
		
		final Function interval = new ConstantFunction(80.0);
		final Function ratio = new ConstantFunction(0.3);
		
		//useful functions for baseline measurement
		final DifficultyFunction baselineDiff = new DifficultyFunction(interval, new SigmoidBaselineFunction(), ratio);

		final long timeLimit = 120*1000;
		
		// constant scenario session
		final Function speedConstant1 = new ConstantFunctionBaseline(-30, 0.85);
		final Function speedConstant3 = new ConstantFunctionBaseline(0, 1);
		final Function speedConstant5 = new ConstantFunctionBaseline(30, 1.15);
		
		final ScenarioRound cBaselineRound = new ScenarioRound(true, timeLimit, baselineDiff);
		final ScenarioRound cSr1 = new ScenarioRound(false, timeLimit, new DifficultyFunction(interval, speedConstant1, ratio));
		final ScenarioRound cSr3 = new ScenarioRound(false, timeLimit, new DifficultyFunction(interval, speedConstant3, ratio));
		final ScenarioRound cSr5 = new ScenarioRound(false, timeLimit, new DifficultyFunction(interval, speedConstant5, ratio));
		
		final ScenarioSession constantScenarioSession = new ScenarioSession();
		constantScenarioSession.add(cBaselineRound);
		constantScenarioSession.add(cSr1);
		constantScenarioSession.add(cSr3);
		constantScenarioSession.add(cSr5);
		em.persist(constantScenarioSession);
		
		// linear scenario session
		final Function speedLinear1 = new LinearFunctionBaseline(-30, 0.85);
		final Function speedLinear3 = new LinearFunctionBaseline(0, 1);
		final Function speedLinear5 = new LinearFunctionBaseline(30, 1.15);

		final ScenarioRound dBaselineRound = new ScenarioRound(true, timeLimit, baselineDiff);
		final ScenarioRound dSr1 = new ScenarioRound(false, timeLimit, new DifficultyFunction(interval, speedLinear1, ratio));
		final ScenarioRound dSr3 = new ScenarioRound(false, timeLimit, new DifficultyFunction(interval, speedLinear3, ratio));
		final ScenarioRound dSr5 = new ScenarioRound(false, timeLimit, new DifficultyFunction(interval, speedLinear5, ratio));
		
		final ScenarioSession linearScenarioSession = new ScenarioSession();
		linearScenarioSession.add(dBaselineRound);
		linearScenarioSession.add(dSr1);
		linearScenarioSession.add(dSr3);
		linearScenarioSession.add(dSr5);
		em.persist(linearScenarioSession);
		
		em.getTransaction().commit();
	}
}