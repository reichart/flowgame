package de.tum.in.flowgame.server;

import de.tum.in.flowgame.dao.GameSessionDAO;
import de.tum.in.flowgame.dao.GameSessionDAOImpl;
import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;

public class UploadAction extends GameDataAction {

	@Override
	public Object execute(final Object o) throws Exception {
		if (o instanceof Person) {
			final Person person = (Person) o;
			final PersonDAO pdao = new PersonDAOImpl();
			pdao.update(person);
		} else if (o instanceof GameSession) {
			final GameSession gameSession = (GameSession) o;
			final GameSessionDAO gdao = new GameSessionDAOImpl();
			gdao.update(gameSession);
		} else {
			throw new IllegalArgumentException("Invalid content type: " + o.getClass().getName());
		}

		return null;
	}

}
