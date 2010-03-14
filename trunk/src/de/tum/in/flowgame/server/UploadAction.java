package de.tum.in.flowgame.server;

import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;

public class UploadAction extends GameDataAction<Object, Object> {

	@Override
	public Object execute(final Object o) throws Exception {
		if (o instanceof Person) {
			final Person person = (Person) o;
			em.merge(person);
		} else if (o instanceof GameSession) {
			final GameSession gameSession = (GameSession) o;
			em.merge(gameSession);
		} else {
			throw new IllegalArgumentException("Invalid content type: " + o.getClass().getName());
		}

		return null;
	}

}
