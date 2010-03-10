package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.Highscore;

public class HighscoresDownloadAction extends GameDataAction {

	@Override
	public Object execute(final Object persons) throws Exception {
		final PersonDAO personDAO = new PersonDAOImpl();
		if (persons instanceof List<?>) {
			List<Highscore> highscores = personDAO.getHighscores((List<Long>) persons);
			return highscores;
		} else {
			throw new Exception("The given parameters are not a list of person ids.");
		}
	}

}
