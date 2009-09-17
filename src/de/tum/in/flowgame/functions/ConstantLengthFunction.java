package de.tum.in.flowgame.functions;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import de.tum.in.flowgame.model.Function;

@Entity
public class ConstantLengthFunction extends Function implements Serializable {

	private Function speedFunction;
	@Transient
	private double speed;
	@Transient
	private double initialSpeed;
	@Transient
	private double factor;
	
	public ConstantLengthFunction() {
		//empty
	}
	
	public ConstantLengthFunction(double initialValue, Function speedFunction) {
		super(initialValue);
		this.speedFunction = speedFunction;
		this.initialSpeed = speedFunction.getInitialValue();
	}

	@Override
	public double getValue(Long time) {
		initialSpeed = speedFunction.getInitialValue();
		speed = speedFunction.getValue(time);
		factor = initialSpeed/speed;
		double initialDelta = initialValue/initialSpeed * 1000;
		return factor * initialDelta;
	}

	@Override
	public double getInitialValue() {
		return initialValue;
	}

}
