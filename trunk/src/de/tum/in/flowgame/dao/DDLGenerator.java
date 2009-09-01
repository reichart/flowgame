package de.tum.in.flowgame.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

			Function f1 = new ConstantFunction(3.0f);
			Function f2 = new LinearFunction();

			DifficultyFunction df = new DifficultyFunction();
			df.setIntervald(f1);
			df.setRatio(f2);
			df.setSpeed(f1);
			
			ScenarioRound sr = new ScenarioRound();
			sr.setBaselineModifier(1);
			sr.setDifficutyFunction(df);
			sr.setExpectedPlaytime(2000L);
			sr.setQuestionnaire(qn);
			
			ScenarioSession ss = new ScenarioSession();
			ss.getRounds().add(sr);

			em.getTransaction().begin();
			em.persist(q);
			em.persist(qn);
			em.persist(ans);
			em.persist(p);
			em.persist(d);
			em.persist(f1);
			em.persist(f2);
			em.persist(df);
			em.persist(ss);
			em.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}