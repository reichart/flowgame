package de.tum.in.flowgame.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mysql.jdbc.Driver;

import de.tum.in.flowgame.functions.ConstantFunction;
import de.tum.in.flowgame.functions.ConstantLengthFunction;
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

			Question q = new Question();
			q.setText("Alle meine Entchens?");
			Questionnaire qn = new Questionnaire();
			qn.setName("Test");
			qn.setDescription("So was aber auch");
			qn.addQuestion(q);
			Answer ans = new Answer(q, 1);
			Person p = new Person(12345L);
			p.setName("Ballala");
			Difficulty d = new Difficulty();

			Function intervalFunction = new ConstantFunction(400);
			Function ratioFunction = new ConstantFunction(0.6);
			LinearFunction speedFunction = new LinearFunction(30D,4.0d/600);
			Function constantLengthIntervalFunction  = new ConstantLengthFunction(60.0, speedFunction);

			DifficultyFunction df = new DifficultyFunction();
			df.setIntervald(constantLengthIntervalFunction);
//			df.setIntervald(intervalFunction);
			df.setRatio(ratioFunction);
			df.setSpeed(speedFunction);
			
			ScenarioRound sr = new ScenarioRound();
			sr.setBaselineModifier(1);
			sr.setDifficutyFunction(df);
			sr.setExpectedPlaytime(2000L);
			
			ScenarioRound sr2 = new ScenarioRound();
			sr2.setBaselineModifier(5);
			sr2.setDifficutyFunction(df);
			sr2.setExpectedPlaytime(2000L);
			sr2.setQuestionnaire(qn);
			
			ScenarioSession ss = new ScenarioSession();
			ss.getRounds().add(sr);
			ss.getRounds().add(sr2);

			em.getTransaction().begin();
			em.persist(q);
			em.persist(qn);
			em.persist(ans);
			em.persist(p);
			em.persist(d);
			em.persist(intervalFunction);
			em.persist(speedFunction);
			em.persist(constantLengthIntervalFunction);
			em.persist(df);
			em.persist(ss);
			em.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}