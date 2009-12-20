package de.tum.in.flowgame;

import java.util.LinkedList;
import java.util.List;

/**
 * This class provides Information about the success of the player.
 * <p>
 * It collects information about the number of collisions with a type of
 * {@link Collidable} and provides four ratios:<br>
 * <ul>
 * <li>shortRatio ({@link #getShortRatio()}) - where the last three passed
 * collidables are recorded.</li>
 * <li>midRatio ({@link #getMidRatio()}) - where the last ten passed collidables
 * are recorded.</li>
 * <li>longRatio ({@link #getlongRatio()}) - where the last thirty passed
 * collidables are recorded.</li>
 * <li>completeRatio ({@link #getCompleteRatio()}) - where all passed
 * collidables are recorded.</li>
 * </ul>
 * 
 * The default values (3, 10, 30) can be overridden in the constructor (
 * {@link #Trend(int, int, int)}).
 */
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

	/**
	 * Creates a Trend class with default ranges.
	 */
	public Trend() {
		// empty
	}

	/**
	 * Creates a Trend class with user defined values for the ranges.
	 * 
	 * @param shortRange
	 *            Base number of {@link Collidable}s for calculating a short time ratio.
	 * @param midRange
	 *            Base number of {@link Collidable}s for calculating a mid time ratio.
	 * @param longRange
	 *            Base number of {@link Collidable}s for calculating a long time ratio.
	 * <p>           
	 * It should be: shortRange < midRange < longRange.
	 */
	public Trend(int shortRange, int midRange, int longRange) {
		this.shortRange = shortRange;
		this.midRange = midRange;
		this.longRange = longRange;
	}

	/**
	 * This method updates all ratios.
	 * @param collision Should be <code>true</code>, if {@link Collidable} was hit, else <code>false</code>
	 */
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

	/**
	 * @return Number of collisions with the last three {@link Collidable}s
	 *         divided through three. The number three can be overridden in the
	 *         constructor.
	 */
	public float getShortRatio() {
		return shortRatio;
	}

	/**
	 * @return Number of collisions with the last ten {@link Collidable}s
	 *         divided through ten. The number ten can be overridden in the
	 *         constructor
	 */
	public float getMidRatio() {
		return midRatio;
	}

	/**
	 * @return Number of collisions with the last thirty {@link Collidable}s of
	 *         a certain type divided through ten. The number thirty can be
	 *         overridden in the constructor.
	 */
	public float getLongRatio() {
		return longRatio;
	}

	/**
	 * @return Number of collisions with all {@link Collidable}s of a certain
	 *         type divided through the total amount of these {@link Collidable}s.
	 */
	public float getCompleteRatio() {
		return completeRatio;
	}
}
