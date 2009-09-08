package de.tum.in.flowgame.server;

import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.Person;

public class PersonDownloadAction extends GameDataAction {

	@Override
	public Object execute(final Object id) throws Exception {
		final PersonDAO persons = new PersonDAOImpl();
		final Person person = persons.find((Long) id);
		if (person == null) {
			throw new NullPointerException("No person found for id " + id);
		} else {
			return person;
		}
	}

}
