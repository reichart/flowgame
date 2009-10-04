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
public class GameRound extends AbstractEntity implements Serializable, GameListener {
	ScenarioRound scenarioRound;
	Long actualPlaytime;
	List<TimeDifficultyPair> difficultyByTime;
	@OneToMany(cascade = CascadeType.ALL)
	List<Collision> collisions;
	List<Answer> answers;
	private transient long startTime;
	Long score;

	public GameRound() {
		this.difficultyByTime = new ArrayList<TimeDifficultyPair>();
		this.collisions = new ArrayList<Collision>();
		this.answers = new ArrayList<Answer>();
	}

	public Long getActualPlaytime() {
		return actualPlaytime;
	}

	public ScenarioRound getScenarioRound() {
		return scenarioRound;
	}

	public void setScenarioRound(final ScenarioRound scenarioRound) {
		this.scenarioRound = scenarioRound;
	}

	public List<TimeDifficultyPair> getDifficultyByTime() {
		return difficultyByTime;
	}

	public void setDifficultyByTime(final List<TimeDifficultyPair> difficultyByTime) {
		this.difficultyByTime = difficultyByTime;
	}

	public List<Collision> getCollisions() {
		return collisions;
	}

	public void setCollisions(final List<Collision> collisions) {
		this.collisions = collisions;
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

	public Long getScore() {
		return score;
	}

	public void setScore(final Long score) {
		this.score = score;
	}

	@Override
	public void collided(final GameLogic logic, final Item item) {
		getCollisions().add(new Collision(item));
	}

	@Override
	public void gamePaused(final GameLogic game) {
		// empty
	}

	@Override
	public void gameResumed(final GameLogic game) {
		// empty
	}

	@Override
	public void gameStarted(final GameLogic game) {
		setStartTime(System.currentTimeMillis());
	}

	@Override
	public void gameStopped(final GameLogic game) {
		setActualPlaytime(System.currentTimeMillis() - getStartTime());
		game.removeListener(this);
	}

	@Override
	public void sessionFinished(final GameLogic game) {
		// empty
	}
}