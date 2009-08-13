package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class Function extends AbstractEntity implements Serializable {
	
	protected enum FunctionType{CONSTANT, LINEAR, EXPONENTIAL}
	
	@Transient
	protected double initialValue;
	protected FunctionType functionType;
	
	public abstract double getValue(Long time);
	
}
