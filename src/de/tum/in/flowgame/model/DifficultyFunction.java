package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class DifficultyFunction extends AbstractEntity {
	@Transient
	private static final double INTERVAL_MAX_VALUE = 1000;

	@Column(nullable = false)
	@ManyToOne(cascade = CascadeType.ALL)
	private Function interval;

	@Column(nullable = false)
	@ManyToOne(cascade = CascadeType.ALL)
	private Function speed;

	@Column(nullable = false)
	@ManyToOne(cascade = CascadeType.ALL)
	private Function ratio;

	@SuppressWarnings("unused")
	private DifficultyFunction() {
		// for JPA
	}
	
	public DifficultyFunction(final Function interval, final Function speed, final Function ratio) {
		this.interval = interval;
		this.speed = speed;
		this.ratio = ratio;
	}

	public Function getInterval() {
		return interval;
	}

	public Function getSpeed() {
		return speed;
	}

	public Function getRatio() {
		return ratio;
	}

	public double getDifficultyRating(final long time) {
		final double intervalValue = interval.getValue(time);
		final double speedValue = speed.getValue(time);
		final double ratioValue = ratio.getValue(time);

		double difficultyValue = (INTERVAL_MAX_VALUE - intervalValue) + speedValue + ratioValue * 500;
		// normalize to one assumed that maximum is 2500 (1000 interval, 1000 speed, 500 ratio)
		difficultyValue /= 2500;
		return difficultyValue;
	}

}
