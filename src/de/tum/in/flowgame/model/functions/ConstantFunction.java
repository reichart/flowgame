package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class ConstantFunction extends Function {

	public ConstantFunction(){
		//empty
	}
	
	public ConstantFunction(double initialValue) {
		super(initialValue);
	}

	@Override
	public double getInitialValue() {
		return initialValue;
	}

	@Override
	public double getValue(double z) {
		return initialValue;
	}

}
