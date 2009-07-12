package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogic.Item;

@Entity
public class GameRound extends AbstractEntity implements GameListener {
	ScenarioRound scenarioRound;
	Long actualPlaytime;
	List<TimeDifficultyPair> difficultyByTime;
	@OneToMany(cascade = CascadeType.ALL)
	List<Collision> collisions;
	List<Answer> answers;

	private transient long startTime;

	public GameRound() {
		this.difficultyByTime = new ArrayList<TimeDifficultyPair>();
		this.collisions = new ArrayList<Collision>();
		this.answers = new ArrayList<Answer>();
	}

	public Long getActualPlaytime() {
		return actualPlaytime;
	}

	@Override
	public void gameStarted(final GameLogic game) {
		this.actualPlaytime = null;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public void gamePaused(final GameLogic game) {
		// TODO impl
	}

	public void gameStopped(final GameLogic game) {
		this.actualPlaytime = (System.currentTimeMillis() - startTime);
	}
	
	@Override
	public void collided(final GameLogic logic, final Item item) {
		this.collisions.add(new Collision(item));
	}

	@Override
	public void gameResumed(GameLogic game) {
		// TODO Auto-generated method stub
		
	}
}