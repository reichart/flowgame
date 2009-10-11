package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class ScenarioRound extends AbstractEntity implements Serializable {
	Integer baselineModifier;
	Long expectedPlaytime;
	DifficultyFunction difficultyFunction;
	Questionnaire questionnaire;
	Boolean baseline;
	
	public ScenarioRound() {
		baseline = false;
	}

	public Integer getBaselineModifier() {
		return baselineModifier;
	}

	public void setBaselineModifier(Integer baselineModifier) {
		this.baselineModifier = baselineModifier;
	}

	public Long getExpectedPlaytime() {
		return expectedPlaytime;
	}

	public void setExpectedPlaytime(Long expectedPlaytime) {
		this.expectedPlaytime = expectedPlaytime;
	}

	public DifficultyFunction getDifficultyFunction() {
		return difficultyFunction;
	}

	public void setDifficultyFunction(DifficultyFunction difficultyFunction) {
		this.difficultyFunction = difficultyFunction;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	
	public Boolean isBaselineRound() {
		return baseline;
	}

	public void setBaselineRound(Boolean baseline) {
		this.baseline = baseline;
	}
}