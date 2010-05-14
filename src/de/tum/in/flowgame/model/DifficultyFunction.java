package de.tum.in.flowgame.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class DifficultyFunction extends AbstractEntity {
	@Transient
	private static final double INTERVAL_MAX_VALUE = 1000;

	@JoinColumn(nullable = false)
	@ManyToOne(cascade = CascadeType.ALL)
	private Function interval;

	@JoinColumn(nullable = false)
	@ManyToOne(cascade = CascadeType.ALL)
	private Function speed;

	@JoinColumn(nullable = false)
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

//	public double getDifficultyRating(final long time) {
//		return getDifficultyRating(time);
//	}
	
	public double getDifficultyRating(final double position) {
		final double intervalValue = interval.getValue(position);
		final double speedValue = speed.getValue(position);
		final double ratioValue = ratio.getValue(position);

		double difficultyValue = (INTERVAL_MAX_VALUE - intervalValue) + speedValue + ratioValue * 500;
		// normalize to one assumed that maximum is 2500 (1000 interval, 1000 speed, 500 ratio)
		difficultyValue /= 2500;
		return difficultyValue;
	}

}
