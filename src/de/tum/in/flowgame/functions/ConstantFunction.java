package de.tum.in.flowgame.functions;

import java.io.Serializable;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class ConstantFunction extends Function implements Serializable {
	
	private ConstantFunction() {
		//empty
	}
	
	public ConstantFunction(double initialValue) {
		this.initialValue = initialValue;
	}

	@Override
	public double getValue(Long time) {
		return initialValue;
	}
	
}
