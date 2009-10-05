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
	private int hitsShort = 0;
	private int hitsMid = 0;
	private int hitsLong = 0;
	private int hitsComplete = 0;
	private List<Boolean> items = new LinkedList<Boolean>();

	public Trend() {
		// empty
	}

	public Trend(int shortRange, int midRange, int longRange) {
		this.shortRange = shortRange;
		this.midRange = midRange;
		this.longRange = longRange;
	}

	public void update(boolean collision) {
		items.add(collision);
		hitsShort += calculateChange(shortRange, collision);
		hitsMid += calculateChange(midRange, collision);
		hitsLong += calculateChange(longRange, collision);
		if (collision)
			hitsComplete++;
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
		shortRatio = hitsShort / (float) sr;
		midRatio = hitsMid / (float) mr;
		longRatio = hitsLong / (float) lr;
		completeRatio = hitsComplete / (float) counter;
	}

	private int calculateChange(int range, boolean collision) {
		if (items.size() > range) {

			int value = 0;
			if (collision) {
				value++;
			}
			if (items.get(items.size() - range - 1)) {
				value--;
			}
			return value;
		} else {
			if (collision) {
				return 1;
			}
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
