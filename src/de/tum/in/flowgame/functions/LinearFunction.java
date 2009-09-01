package de.tum.in.flowgame.functions;

import java.io.Serializable;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class LinearFunction extends Function implements Serializable{
	
	public LinearFunction() {
//		initialValue = 0D;
//		functionType = Function.FunctionType.LINEAR;
	}

	public LinearFunction(double initialValue) {
//		this.initialValue = initialValue;
	}

//	@Override
//	public double getValue(Long time) {
//		return initialValue;
//	}
	
}
