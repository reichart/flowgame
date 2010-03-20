package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;

@Entity
public class GameRound extends AbstractEntity implements GameListener {
	private ScenarioRound scenarioRound;
	private Long actualPlaytime;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Collision> collisions;
	private List<Answer> answers;
	private long startTime;
	private long score;
	/**
	 * rank within friends at the time the round was played
	 */
	private Integer rank;

	public GameRound() {
		this.collisions = new ArrayList<Collision>();
		this.answers = new ArrayList<Answer>();
		score = 0L;
		rank = 0;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public ScenarioRound getScenarioRound() {
		return scenarioRound;
	}

	public void setScenarioRound(final ScenarioRound scenarioRound) {
		this.scenarioRound = scenarioRound;
	}

	public List<Collision> getCollisions() {
		return collisions;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(final List<Answer> answers) {
		this.answers = answers;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	public void setActualPlaytime(final Long actualPlaytime) {
		this.actualPlaytime = actualPlaytime;
	}

	public Score getScore() {
		return new Score(startTime, score);
	}

	public void increaseScore(final long points) {
		this.score += points;
		if (score < 0) {
			score = 0L;
		}
	}

	public void added(final GameLogic game) {
		// empty
	}
	
	public void removed(final GameLogic game) {
		// empty
	}
	
	public void collided(final GameLogic logic, final Item item) {
		getCollisions().add(new Collision(item));
	}

	public void gamePaused(final GameLogic game) {
		// empty
	}

	public void gameResumed(final GameLogic game) {
		// empty
	}

	public void gameStarted(final GameLogic game) {
		setStartTime(System.currentTimeMillis());
	}

	public void gameStopped(final GameLogic game) {
		setActualPlaytime(System.currentTimeMillis() - getStartTime());
		game.removeListener(this);
	}

	public void setScore(Long score) {
		this.score = score;		
	}

//	@Override
//	public void sessionFinished(final GameLogic game) {
//		// empty
//	}
}