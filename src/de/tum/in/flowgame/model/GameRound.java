package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class GameRound extends AbstractEntity {
	ScenarioRound scenarioRound;
	Long actualPlaytime;
	List<TimeDifficultyPair> difficultyByTime;
	@OneToMany(cascade = CascadeType.ALL)
	List<Collision> collisions;
	List<Answer> answers;
	
	public GameRound() {
		difficultyByTime = new ArrayList<TimeDifficultyPair>();
		collisions = new ArrayList<Collision>();
		answers = new ArrayList<Answer>();
	}
	
}