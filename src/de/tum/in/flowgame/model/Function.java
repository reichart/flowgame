package de.tum.in.flowgame.model;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Function extends AbstractEntity {
	enum FunctionType{LINEAR, EXPONENTIAL}
	
	@Transient
	double initialValue;
	FunctionType functionType;
		
	double getValue(Long time) {
		return 0;
	}
}
