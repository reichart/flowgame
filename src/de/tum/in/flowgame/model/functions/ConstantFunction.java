package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class ConstantFunction extends Function {

	@SuppressWarnings("unused")
	private ConstantFunction() {
		// JPA only
	}

	public ConstantFunction(final double initialValue) {
		super(initialValue);
	}

	@Override
	public double getValue(final double z) {
		return getInitialValue();
	}

	@Override
	public String toString() {
		return "const[iv=" + getInitialValue() + "]";
	}
}
