package de.tum.in.flowgame.model.functions;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class LnFunction extends Function {

	private double multiplier;
	@Column(name="\"offset\"")
	private double offset;

	@SuppressWarnings("unused")
	private LnFunction() {
		// JPA only
	}

	public LnFunction(final double multiplier, final double offset, final double initialValue) {
		super(initialValue);
		this.multiplier = multiplier;
		this.offset = offset;
	}

	@Override
	public double getValue(final double z) {
		double log = Math.log(z + offset);
		return Double.isNaN(log) ? 0 : multiplier * log + getInitialValue();
	}

	@Override
	public String toString() {
		return "ln[iv=" + getInitialValue() + ";mul=" + multiplier + ";off=" + offset + "]";
	}
}