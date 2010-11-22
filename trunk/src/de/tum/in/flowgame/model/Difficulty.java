package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class Difficulty extends AbstractEntity {

	long intervald;
	long speed;
	float ratio;

	@SuppressWarnings("unused")
	private Difficulty() {
		// empty
	}

	public Difficulty(long intervald, long speed, float ratio) {
		this.intervald = intervald;
		this.speed = speed;
		this.ratio = ratio;
	}

	public long getIntervald() {
		return intervald;
	}

	public long getSpeed() {
		return speed;
	}

	public float getRatio() {
		return ratio;
	}

	@Override
	public String toString() {
		return "difficulty[ivald=" + intervald + ",speed=" + speed + ",ratio=" + ratio + "]";
	}
}