package de.tum.in.flowgame.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Function extends AbstractEntity {

	private double initialValue;

	public Function() {
		// empty
	}

	public Function(final double initialValue) {
		this.initialValue = initialValue;
	}

	public abstract double getValue(double z);

	public final double getInitialValue() {
		return initialValue;
	}

}
