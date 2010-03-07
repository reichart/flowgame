package de.tum.in.flowgame.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.tum.in.flowgame.client.HighscoreRequest;
import de.tum.in.flowgame.dao.GameSessionDAO;
import de.tum.in.flowgame.dao.GameSessionDAOImpl;
import de.tum.in.flowgame.model.Score;

public class PersonalHighscoreDownloadAction extends GameDataAction<HighscoreRequest, List<Score>> {

	private class ScoreComparator implements Comparator<Score> {
		public int compare(Score a, Score b) {
			if (a.getStartTime() == b.getStartTime())
				return 0;
			else if (a.getStartTime() < b.getStartTime())
				return 1;
			else
				return -1;
		}
	}

	@Override
	protected List<Score> execute(final HighscoreRequest highscoreRequest) throws Exception {
		// System.out.println("Execute Highscore Download");
		long personId = highscoreRequest.getPersonId();
		int numElements = highscoreRequest.getNumElements();
		GameSessionDAO gsDAO = new GameSessionDAOImpl();
		List<Score> result = gsDAO.getPersonalScores(personId);
		if (result.size() < numElements)
			numElements = result.size();
		Collections.sort(result, new ScoreComparator());
		List<Score> returnResult = new ArrayList<Score>(result.subList(0, numElements));
		// for (Score s : returnResult){
		// System.out.println("id: " + s.getId());
		// System.out.println("Score: " + s.getScore());
		// }
		return returnResult;
	}

}
