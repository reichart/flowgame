package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class LinearFunction extends Function {
	Double slope;
	
	public LinearFunction(){
		//empty
	}
	
	public LinearFunction(Double initialValue, Double slope) {
		super(initialValue);
		this.slope = slope;
	}

	@Override
	public double getValue(double z) {
		return slope * z + initialValue;
	}

	@Override
	public double getInitialValue() {
		return initialValue;
	}
	
}