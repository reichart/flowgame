package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.tum.in.flowgame.GameLogic.Item;

@Entity
public class Collision extends AbstractEntity {

	@Temporal(TemporalType.TIMESTAMP)
	private final Date time;

	private final Item object;

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
}
