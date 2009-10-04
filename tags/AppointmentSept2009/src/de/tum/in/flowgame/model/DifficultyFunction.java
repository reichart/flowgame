package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DifficultyFunction extends AbstractEntity implements Serializable {
	@ManyToOne(cascade = CascadeType.ALL)
	Function intervald;
	@ManyToOne(cascade = CascadeType.ALL)
	Function speed;
	@ManyToOne(cascade = CascadeType.ALL)
	Function ratio;

	public Function getInterval() {
		return intervald;
	}

	public void setIntervald(Function intervald) {
		this.intervald = intervald;
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
	
}
