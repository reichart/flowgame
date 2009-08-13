package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class DifficultyFunction extends AbstractEntity {
	Function intervald;
	Function speed;
	Function ratio;
}
