package de.tum.in.flowgame.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Score;

public class GameSessionDAOImpl extends GenericJPADAO<GameSession, Integer> implements GameSessionDAO {

	public GameSessionDAOImpl() {
		super("IDP", GameSession.class);
	}
	
	
	public List<Score> getPersonalScores (Person p){
//		HashMap<Integer, Long> result = new HashMap<Integer, Long>();
		List<Score> result = new LinkedList<Score>();
		Query q = getEntityManager().createQuery("SELECT gs FROM GameSession gs WHERE gs.player=:p");
		q.setParameter("p", p);
		List<GameSession> sessionList = q.getResultList();
		Iterator<GameSession> gsIterator = sessionList.iterator();
		while (gsIterator.hasNext()){
			List<GameRound> roundList = gsIterator.next().getRounds();
			Iterator<GameRound> grIterator = roundList.iterator();
			while (grIterator.hasNext()){
				GameRound gr = grIterator.next();
				result.add(new Score(gr.getId(), gr.getScore()));
//				System.out.println("ID: " + gr.getId());
//				System.out.println("Score: " + gr.getScore());
			}
		}
		return result;
	}

}