package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class ScenarioRound extends AbstractEntity implements Serializable {
	Integer baselineModifier;
	Long expectedPlaytime;
	DifficultyFunction difficutyFunction;
	Questionnaire questionnaire;
	
	
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
	public DifficultyFunction getDifficutyFunction() {
		return difficutyFunction;
	}
	public void setDifficutyFunction(DifficultyFunction difficutyFunction) {
		this.difficutyFunction = difficutyFunction;
	}
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}
	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	
	
}