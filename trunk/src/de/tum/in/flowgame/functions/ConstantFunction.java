package de.tum.in.flowgame.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class ConstantFunction extends Function {
	
	public ConstantFunction() {
		initialValue = 0;
		functionType = Function.FunctionType.CONSTANT;
	}

	public ConstantFunction(double initialValue) {
		this.initialValue = initialValue;
	}

	@Override
	public double getValue(Long time) {
		return initialValue;
	}
	
}
