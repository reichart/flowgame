package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.dao.GameSessionDAOImpl;
import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;

public class HighscoreDownloadAction extends GameDataAction<Long, Long> {

	@Override
	public Long execute(final Long id) throws Exception {
		final PersonDAO persons = new PersonDAOImpl();
		final Person person = persons.find(id);
		if (person == null) {
			throw new NullPointerException("No person found for id " + id);
		} else {
			// TODO performance check
			GameSessionDAOImpl gsdao = new GameSessionDAOImpl();
			List<GameSession> gameSessions = gsdao.findAll();
			Long highscore = Long.MIN_VALUE;
			for (GameSession gameSession : gameSessions) {
				if (gameSession.getPlayer().equals(person)) {
					for (GameRound gameRound : gameSession.getRounds()) {
						if (highscore < gameRound.getScore()) {
							highscore = gameRound.getScore();
						}
					}
				}
			}
			return highscore;
		}
	}

}
