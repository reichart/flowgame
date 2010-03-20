package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class DifficultyFunction extends AbstractEntity {
	@Transient
	private static final double intervalMaxValue = 1000;
	
	@ManyToOne(cascade = CascadeType.ALL)
	Function interval;
	@ManyToOne(cascade = CascadeType.ALL)
	Function speed;
	@ManyToOne(cascade = CascadeType.ALL)
	Function ratio;

	public Function getInterval() {
		return interval;
	}

	public void setIntervald(Function interval) {
		this.interval = interval;
	}

	public Function getSpeed() {
		return speed;
	}

	public void setSpeed(Function speed) {
		this.speed = speed;
	}

	public Function getRatio() {
		return ratio;
	}

	public void setRatio(Function ratio) {
		this.ratio = ratio;
	}
	
	public double getDifficultyRating(long time) {
		double intervalValue = interval.getValue(time);
		double speedValue = speed.getValue(time);
		double ratioValue = ratio.getValue(time);
		
		double difficultyValue = (intervalMaxValue - intervalValue) + speedValue + ratioValue * 500;
		//normalize to one assumed that maximum is 2500 (1000 interval, 1000 speed, 500 ratio
		difficultyValue /= 2500;
		return difficultyValue;
	}
	
}
