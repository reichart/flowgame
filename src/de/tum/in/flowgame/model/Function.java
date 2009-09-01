package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Function extends AbstractEntity implements Serializable {
	
	@Transient
	protected Double initialValue;
	
	//public abstract double getValue(Long time);
	
}
