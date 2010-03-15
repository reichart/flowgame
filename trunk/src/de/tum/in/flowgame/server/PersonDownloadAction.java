package de.tum.in.flowgame.server;

import de.tum.in.flowgame.model.Person;

public class PersonDownloadAction extends GameDataAction<Long, Person> {

	@Override
	public Person execute(final Long id) throws Exception {
		final Person person = em.find(Person.class, id);
		
		// prevent lazily-loaded list on client side
		person.setProfilingAnswers(null);
		
		if (person == null) {
			throw new NullPointerException("No person found for id " + id);
		} else {
			return person;
		}
	}

}
