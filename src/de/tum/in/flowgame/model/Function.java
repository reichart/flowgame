package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Function extends AbstractEntity implements Serializable {
	
	protected Double initialValue;
	
	public double getValue(Long time) {
		return 0.5;
	}
	
}
