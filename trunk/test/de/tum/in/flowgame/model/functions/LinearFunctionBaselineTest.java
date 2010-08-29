package de.tum.in.flowgame.model.functions;

import org.junit.Assert;
import org.junit.Test;

public class LinearFunctionBaselineTest {

	private static final double DELTA = 1 / 100d;

	private final LinearFunctionBaseline speedFunctionLinear1 = new LinearFunctionBaseline(-30, 0.85);
	private final LinearFunctionBaseline speedFunctionLinear2 = new LinearFunctionBaseline(-15, 0.925);
	private final LinearFunctionBaseline speedFunctionLinear3 = new LinearFunctionBaseline(0, 1);
	private final LinearFunctionBaseline speedFunctionLinear4 = new LinearFunctionBaseline(15, 1.075);
	private final LinearFunctionBaseline speedFunctionLinear5 = new LinearFunctionBaseline(30, 1.15);

	@Test
	public void testBaseline100() {
		final double baseline = 100;
		final double expectedPlaytime = 60 * 1000;
		
		speedFunctionLinear1.configure(baseline, expectedPlaytime);
		speedFunctionLinear2.configure(baseline, expectedPlaytime);
		speedFunctionLinear3.configure(baseline, expectedPlaytime);
		speedFunctionLinear4.configure(baseline, expectedPlaytime);
		speedFunctionLinear5.configure(baseline, expectedPlaytime);

		Assert.assertEquals(22.5, speedFunctionLinear1.getDifference(), DELTA);
		Assert.assertEquals(43.75, speedFunctionLinear1.getValue(0), DELTA);
		Assert.assertEquals(66.25, speedFunctionLinear1.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(22.5, speedFunctionLinear2.getDifference(), DELTA);
		Assert.assertEquals(66.25, speedFunctionLinear2.getValue(0), DELTA);
		Assert.assertEquals(88.75, speedFunctionLinear2.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(22.5, speedFunctionLinear3.getDifference(), DELTA);
		Assert.assertEquals(88.75, speedFunctionLinear3.getValue(0), DELTA);
		Assert.assertEquals(111.25, speedFunctionLinear3.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(22.5, speedFunctionLinear4.getDifference(), DELTA);
		Assert.assertEquals(111.25, speedFunctionLinear4.getValue(0), DELTA);
		Assert.assertEquals(133.75, speedFunctionLinear4.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(22.5, speedFunctionLinear5.getDifference(), DELTA);
		Assert.assertEquals(133.75, speedFunctionLinear5.getValue(0), DELTA);
		Assert.assertEquals(156.25, speedFunctionLinear5.getValue(expectedPlaytime), DELTA);
	}
	
	@Test
	public void testBaseline241() {
		final double baseline = 241;
		final double expectedPlaytime = 60 * 1000;
		
		speedFunctionLinear1.configure(baseline, expectedPlaytime);
		speedFunctionLinear2.configure(baseline, expectedPlaytime);
		speedFunctionLinear3.configure(baseline, expectedPlaytime);
		speedFunctionLinear4.configure(baseline, expectedPlaytime);
		speedFunctionLinear5.configure(baseline, expectedPlaytime);

		Assert.assertEquals(33.075, speedFunctionLinear1.getDifference(), DELTA);
		Assert.assertEquals(158.31, speedFunctionLinear1.getValue(0), DELTA);
		Assert.assertEquals(191.38, speedFunctionLinear1.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(33.075, speedFunctionLinear2.getDifference(), DELTA);
		Assert.assertEquals(191.38, speedFunctionLinear2.getValue(0), DELTA);
		Assert.assertEquals(224.46, speedFunctionLinear2.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(33.075, speedFunctionLinear3.getDifference(), DELTA);
		Assert.assertEquals(224.46, speedFunctionLinear3.getValue(0), DELTA);
		Assert.assertEquals(257.53, speedFunctionLinear3.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(33.075, speedFunctionLinear4.getDifference(), DELTA);
		Assert.assertEquals(257.53, speedFunctionLinear4.getValue(0), DELTA);
		Assert.assertEquals(290.61, speedFunctionLinear4.getValue(expectedPlaytime), DELTA);
		
		Assert.assertEquals(33.075, speedFunctionLinear5.getDifference(), DELTA);
		Assert.assertEquals(290.61, speedFunctionLinear5.getValue(0), DELTA);
		Assert.assertEquals(290.61+33.075, speedFunctionLinear5.getValue(expectedPlaytime), DELTA);
	}

}
