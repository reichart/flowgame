package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class LinearFunction extends Function {

	private double slope;

	@SuppressWarnings("unused")
	private LinearFunction() {
		// empty
	}

	public LinearFunction(final double initialValue, final double slope) {
		super(initialValue, 1);
		this.slope = slope;
	}

	@Override
	public double getValue(final double z) {
		return slope * z + getInitialValue();
	}

	@Override
	public String toString() {
		return "linear[iv=" + getInitialValue() + ";slope=" + slope + "]";
	}
}
