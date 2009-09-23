package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Function extends AbstractEntity implements Serializable {
	
	protected double initialValue;
	
	public Function(){
		//empty
	}
	
	public Function(double initialValue){
		this.initialValue = initialValue;
	}
	
	public abstract double getValue(double z);
	
	public abstract double getInitialValue();
		
}
