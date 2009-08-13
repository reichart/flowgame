package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class ScenarioSession extends AbstractEntity implements Serializable{
	List<ScenarioRound> rounds;
	
	public ScenarioSession() {
		rounds = new ArrayList<ScenarioRound>();
	}
	
}
