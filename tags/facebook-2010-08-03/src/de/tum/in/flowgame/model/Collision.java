package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Collision extends AbstractEntity {

	public enum Item {
		FUELCAN, ASTEROID
	}
	
	private final long time;

	private final Item object;

	@SuppressWarnings("unused") // for JPA
	private Collision() {
		this.object = null;
		this.time = 0;
	}
	
	public Collision(final Item object) {
		this.object = object;
		this.time = System.currentTimeMillis();
	}

	public Date getTime() {
		return new Date(time);
	}

	public Item getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "collision with " + object + " at " + time;
	}
}
