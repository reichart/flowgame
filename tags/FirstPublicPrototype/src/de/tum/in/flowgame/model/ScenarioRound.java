package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class ScenarioRound extends AbstractEntity {
	int baselineModifier;
	long expectedPlaytime;
	DifficultyFunction difficutyFunction;
	Questionnaire questionnaire;
}