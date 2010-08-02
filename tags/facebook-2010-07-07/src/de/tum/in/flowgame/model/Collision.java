package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Collision extends AbstractEntity {

	public enum Item {
		FUELCAN, ASTEROID
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	private final Date time;

	private final Item object;

	@SuppressWarnings("unused") // for JPA
	private Collision() {
		this.object = null;
		this.time = new Date();
	}
	
	public Collision(final Item object) {
		this.object = object;
		this.time = new Date();
	}

	public Date getTime() {
		return time;
	}

	public Item getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "collision with " + object + " at " + time;
	}
}
