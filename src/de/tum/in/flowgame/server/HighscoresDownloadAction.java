package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.Highscore;

public class HighscoresDownloadAction extends GameDataAction<List<Long>, List<Highscore>> {

	@Override
	public List<Highscore> execute(final List<Long> persons) throws Exception {
		final PersonDAO personDAO = new PersonDAOImpl();
		return personDAO.getHighscores(persons);
	}

}
