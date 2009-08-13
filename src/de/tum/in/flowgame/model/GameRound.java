package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class GameRound extends AbstractEntity implements Serializable {
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

	public ScenarioRound getScenarioRound() {
		return scenarioRound;
	}

	public void setScenarioRound(ScenarioRound scenarioRound) {
		this.scenarioRound = scenarioRound;
	}

	public List<TimeDifficultyPair> getDifficultyByTime() {
		return difficultyByTime;
	}

	public void setDifficultyByTime(List<TimeDifficultyPair> difficultyByTime) {
		this.difficultyByTime = difficultyByTime;
	}

	public List<Collision> getCollisions() {
		return collisions;
	}

	public void setCollisions(List<Collision> collisions) {
		this.collisions = collisions;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setActualPlaytime(Long actualPlaytime) {
		this.actualPlaytime = actualPlaytime;
	}

}