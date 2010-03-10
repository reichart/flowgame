package de.tum.in.flowgame.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tum.in.flowgame.model.Highscore;
import de.tum.in.flowgame.model.Person;

public class PersonDAOImpl extends GenericJPADAO<Person, Long> implements PersonDAO {

	public PersonDAOImpl() {
		super("IDP", Person.class);
	}
	
	@SuppressWarnings("unchecked")
	private List<Highscore> getGlobalHighscore() {
		return transform(getEntityManager().createQuery("SELECT p.id, MAX(r.score) AS highscore" +
				" FROM Person p, GameSession s, IN (s.rounds) r" +
				" WHERE s.player.id = p.id" + 
				" GROUP BY p.id" + 
				" ORDER BY highscore")
				.getResultList());
	}
	
	@SuppressWarnings("unchecked")
	private List<Highscore> getPersonsHighscore(final List<Long> persons) {
		return transform(getEntityManager().createQuery("SELECT p.id, MAX(r.score) AS highscore" +
				" FROM Person p, GameSession s, IN (s.rounds) r" +
				" WHERE s.player.id = p.id" + 
				" AND p.id IN :personids" +
				" GROUP BY p.id" + 
				" ORDER BY highscore")
			.setParameter("personids", persons)
			.getResultList());
	}
	
	public List<Highscore> getHighscores(List<Long> persons) {
		List<Highscore> globalHighscore = getGlobalHighscore();
		List<Highscore> personsHighscore = getPersonsHighscore(persons);
		
		List<Highscore> friendsHighscore = new ArrayList<Highscore>();
		
		for (Highscore highscore : personsHighscore) {
			Integer percentage = (int) (globalHighscore.indexOf(highscore) *100.0 / (globalHighscore.size()-1) );
			friendsHighscore.add(new Highscore(highscore.getPersonid(), highscore.getScore(), percentage));
		}
		
		return friendsHighscore;
	}
	
	public static void main(final String[] args) {
		final PersonDAOImpl personDAO = new PersonDAOImpl();
		personDAO.print(personDAO.getGlobalHighscore());
		System.err.println("----");
		personDAO.print(personDAO.getPersonsHighscore(Arrays.asList(1071363107L, 226900023L)));
		personDAO.print(personDAO.getHighscores(Arrays.asList(1071363107L, 226900023L)));
	}
	
	private List<Highscore> transform(final List<Object[]> highscore) {
		List<Highscore> list = new ArrayList<Highscore>();
		for (Object[] objects : highscore) {
			list.add(new Highscore((Long) objects[0],(Long) objects[1]));
		}
		return list;
	}

	private void print(final List<Highscore> highscore) {
		for (final Highscore objects : highscore) {
			final long id = objects.getPersonid();
			final long score = objects.getScore();
			
			System.err.println(find(id).getName() + "(" + id + "): " + score + ", " + objects.getPercentage());
		}
	}

}