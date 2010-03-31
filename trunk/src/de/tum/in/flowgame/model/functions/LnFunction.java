package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class LnFunction extends Function {

	private double multiplier;
	private double offset;
	
	@SuppressWarnings("unused")
	private LnFunction() {
		// JPA only
	}
	
	public LnFunction(final double multiplier, final double offset, final double initialValue) {
		super (initialValue);
		this.multiplier = multiplier;
		this.offset = offset;
	}
	
	@Override
	public double getValue(final double z) {
		return multiplier * Math.log(z + offset) + getInitialValue();
	}

}