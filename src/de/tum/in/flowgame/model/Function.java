package de.tum.in.flowgame.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Function extends AbstractEntity {

	private double initialValue;
	private double baselineFactor;
	@Transient
	private final double initialValueCopy;

	public Function() {
		initialValueCopy = 0;
	}

	public Function(final double initialValue, final double baselineFactor) {
		this.initialValue = initialValue;
		initialValueCopy = initialValue;
		this.baselineFactor = baselineFactor;
	}

	public abstract double getValue(double z);

	public final double getInitialValue() {
		return initialValue;
	}

	public void configure(final double baseline, double expectedPlaytime) {
		initialValue = baseline * baselineFactor + initialValueCopy;
	}

}