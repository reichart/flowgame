package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class DifficultyFunction extends AbstractEntity {
	Function interval;
	Function speed;
	Function ratio;
}
