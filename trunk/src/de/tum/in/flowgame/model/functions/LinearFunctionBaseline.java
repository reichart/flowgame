package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;
import javax.persistence.Transient;

import de.tum.in.flowgame.model.Function;

@Entity
public class LinearFunctionBaseline extends Function {

	@Transient
	private double slope;
	@Transient
	private double difference;

	@SuppressWarnings("unused")
	private LinearFunctionBaseline() {
		// empty
	}

	public LinearFunctionBaseline(final double initialValue, final double baselineFactor) {
		super(initialValue, baselineFactor);
	}

	@Override
	public void configure(double baseline, double expectedPlaytime) {
		super.configure(baseline, expectedPlaytime);
		difference = 30 + baseline * 0.150;
		slope = difference / expectedPlaytime;
	}
	
	@Override
	public double getValue(final double z) {
		return slope * z + getInitialValue() - difference/2;
	}

	@Override
	public String toString() {
		return "linear[iv=" + getInitialValue() + ";slope=" + slope + "]";
	}
	
	public double getDifference() {
		return difference;
	}
	
}
