package de.tum.in.flowgame.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Difficulty;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class DDLGenerator {

	public static void main(String[] args) {
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDP");
			EntityManager em = emf.createEntityManager();
			
			Question q = new Question();
			q.setText("Alle meine Entchens?");
			Questionnaire qn = new Questionnaire();
			qn.setName("Test");
			qn.addQuestion(q);
			Answer ans = new Answer(q, 1);
			Person p = new Person(123456L);
			Difficulty d = new Difficulty();
						
			em.getTransaction().begin();
				em.persist(q);
				em.persist(qn);
				em.persist(ans);
				em.persist(p);
				em.persist(d);
			em.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}