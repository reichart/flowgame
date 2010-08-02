package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TimeDifficultyPair extends AbstractEntity {
	@Temporal(TemporalType.TIMESTAMP)
	Date time;
	Difficulty difficulty;
}