package de.tum.in.flowgame.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import oracle.toplink.essentials.exceptions.DatabaseException;
import de.tum.in.flowgame.dao.AnswerDAO;
import de.tum.in.flowgame.dao.AnswerDAOImpl;
import de.tum.in.flowgame.dao.QuestionDAO;
import de.tum.in.flowgame.dao.QuestionDAOImpl;
import de.tum.in.flowgame.dao.QuestionnaireDAO;
import de.tum.in.flowgame.dao.QuestionnaireDAOImpl;

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
			Person p = new Person();
			
			em.getTransaction().begin();
				em.persist(q);
				em.persist(qn);
				em.persist(ans);
				em.persist(p);
			em.getTransaction().commit();
		} catch (DatabaseException ex) {
//			JOptionPane.showMessageDialog(null, "Es ist ein Fehler mit der Datenbank aufgetreten. Überprüfen Sie:\n- ob die Datenbank existiert,\n- ob die Verbindungsdaten korrekt sind", "Datenbankenfehler", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} catch (RollbackException ex) {
//			JOptionPane.showMessageDialog(null, "Die Datensätze existieren bereits.", "Datenbankenfehler", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(null, ex);
			ex.printStackTrace();
		}
	}
}