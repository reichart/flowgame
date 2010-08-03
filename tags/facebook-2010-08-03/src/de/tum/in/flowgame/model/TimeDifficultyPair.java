package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class TimeDifficultyPair extends AbstractEntity {
	long time;
	Difficulty difficulty;
}