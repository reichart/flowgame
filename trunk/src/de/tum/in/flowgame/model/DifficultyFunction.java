package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class DifficultyFunction extends AbstractEntity implements Serializable {
	Function intervald;
	Function speed;
	Function ratio;
}
