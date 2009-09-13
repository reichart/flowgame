package de.tum.in.flowgame;

import java.util.LinkedList;
import java.util.List;

public class Trend {
	private float shortRatio;
	private float midRatio;
	private float longRatio;
	private float completeRatio;
	private int shortRange = 3;
	private int midRange = 10;
	private int longRange = 30;
	private int positiveShort = 0;
	private int positiveMid = 0;
	private int positiveLong = 0;
	private int positiveComplete = 0;
	private List<Boolean> items = new LinkedList<Boolean>();

	public Trend() {
	}

	public Trend(int shortRange, int midRange, int longRange) {
		this.shortRange = shortRange;
		this.midRange = midRange;
		this.longRange = longRange;
	}

	public void update(boolean collision) {
		items.add(collision);
		positiveShort += calculateChange(shortRange, collision);
		positiveMid += calculateChange(midRange, collision);
		positiveLong += calculateChange(longRange, collision);
		if (collision)
			positiveComplete++;
		int counter = items.size();
		int sr = shortRange;
		int mr = midRange;
		int lr = longRange;
		if (counter < shortRange)
			sr = counter;
		if (counter < midRange)
			mr = counter;
		if (counter < longRange)
			lr = counter;
		shortRatio = positiveShort / (float) sr;
		midRatio = positiveMid / (float) mr;
		longRatio = positiveLong / (float) lr;
		completeRatio = positiveComplete / (float) counter;
	}

	private int calculateChange(int range, boolean collision) {
		if (items.size() > range) {
			if ((items.get(items.size() - range - 1) == false) && collision) return 1;
			if ((items.get(items.size() - range - 1) == true) && collision) return 0;
			if ((items.get(items.size() - range - 1) == false) && !collision) return 0;
			if ((items.get(items.size() - range - 1) == true) && !collision) return -1;
		} else {
			if (collision) return 1;
		}
		return 0;
	}

	public float getShortRatio() {
		return shortRatio;
	}

	public float getMidRatio() {
		return midRatio;
	}

	public float getLongRatio() {
		return longRatio;
	}

	public float getCompleteRatio() {
		return completeRatio;
	}
}
