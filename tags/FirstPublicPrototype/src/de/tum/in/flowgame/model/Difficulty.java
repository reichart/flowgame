package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class Difficulty extends AbstractEntity {
	long interval;
	long speed;
	float ratio;
}
