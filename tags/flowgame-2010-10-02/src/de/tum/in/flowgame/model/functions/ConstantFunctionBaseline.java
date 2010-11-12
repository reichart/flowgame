package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class ConstantFunctionBaseline extends Function {

	@SuppressWarnings("unused")
	private ConstantFunctionBaseline() {
		// JPA only
	}

	public ConstantFunctionBaseline(final double initialValue, final double baselineFactor) {
		super(initialValue, baselineFactor);
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
