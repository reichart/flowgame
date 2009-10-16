package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Difficulty extends AbstractEntity implements Serializable {

	long intervald;
	long speed;
	float ratio;
	
	public Difficulty() {
		// empty
	}

}