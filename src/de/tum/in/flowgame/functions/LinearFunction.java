package de.tum.in.flowgame.functions;

import java.io.Serializable;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class LinearFunction extends Function implements Serializable{
	Double slope;
	
	public LinearFunction(){
		//empty
	}
	
	public LinearFunction(Double initialValue, Double slope) {
		super(initialValue);
		this.slope = slope;
	}

	@Override
	public double getValue(Long time) {
		return slope * time + initialValue;
	}

	@Override
	public double getInitialValue() {
		return initialValue;
	}
	
}
