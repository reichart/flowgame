package de.tum.in.flowgame.server;

import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.Person;

public class PersonDownloadAction extends GameDataAction {

	private final PersonDAO persons;

	public PersonDownloadAction() {
		this.persons = new PersonDAOImpl();
	}

	@Override
	public Object execute(final Object id) throws Exception {
		final Person person = persons.find((Long) id);
		if (person == null) {
			throw new NullPointerException("No person found for id " + id);
		} else {
			return person;
		}
	}

}
