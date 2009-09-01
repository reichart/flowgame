package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class ScenarioSession extends AbstractEntity implements Serializable{
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	List<ScenarioRound> rounds;
	String name;
	
	public ScenarioSession() {
		rounds = new ArrayList<ScenarioRound>();
	}

	public List<ScenarioRound> getRounds() {
		return rounds;
	}
	
}
