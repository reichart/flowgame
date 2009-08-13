package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class ScenarioRound extends AbstractEntity implements Serializable {
	int baselineModifier;
	long expectedPlaytime;
	DifficultyFunction difficutyFunction;
	Questionnaire questionnaire;
}