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
	private Double initialValueCopy;

	public Function() {
		initialValueCopy = null;
	}

	public Function(final double initialValue, final double baselineFactor) {
		this.initialValue = initialValue;
		this.initialValueCopy = initialValue;
		this.baselineFactor = baselineFactor;
	}

	public abstract double getValue(double z);

	public final double getInitialValue() {
		return initialValue;
	}

	public void configure(final double baseline, double expectedPlaytime) {
		if (initialValueCopy == null) {
			initialValueCopy = initialValue;
		}
		initialValue = baseline * baselineFactor + initialValueCopy;
	}

}