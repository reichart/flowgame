package de.tum.in.flowgame.model.functions;

import javax.persistence.Entity;

import de.tum.in.flowgame.model.Function;

@Entity
public class SigmoidBaselineFunction extends Function {

	@Override
	public double getValue(double z) {
		if (z < 0) {
			return 30 * Math.tanh(z/5000) + 60;
		} else {
			return 240 * Math.tanh(z/5000) + 60;
		}
	}



}
