package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;

@Entity
public class GameRound extends AbstractEntity {
	private ScenarioRound scenarioRound;
	private long actualPlaytime;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Collision> collisions;
	private List<Answer> answers;
	private long startTime;
	private long score;
	/**
	 * rank within friends at the time the round was played
	 */
	private Integer rank;
	
	@Transient
	private transient GameListener listener;

	@SuppressWarnings("unused")
	private GameRound() { // for JPA
		this(null);
	}

	public GameRound(final ScenarioRound scenarioRound) {
		this.collisions = new ArrayList<Collision>();
		this.answers = new ArrayList<Answer>();
		this.startTime = System.currentTimeMillis();
		this.scenarioRound = scenarioRound;
		this.listener = new DefaultGameListener() {
			@Override
			public void gameStarted(final GameLogic game) {
				setStartTime(System.currentTimeMillis());
			}

			@Override
			public void gameStopped(final GameLogic game) {
				actualPlaytime = game.getElapsedTime();
				game.removeListener(this);
			}
		};
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

	public Score getScore() {
		return new Score(startTime, score);
	}

	public void increaseScore(final long points) {
		this.score += points;
		if (score < 0) {
			score = 0L;
		}
	}

	public void setScore(Long score) {
		this.score = score;		
	}

	public GameListener getListener() {
		return listener;
	}

}