package de.tum.in.flowgame.model.functions;

import org.junit.Assert;
import org.junit.Test;

public class ConstantFunctionBaselineTest {

	private static final double DELTA = 1 / 100d;

	private final ConstantFunctionBaseline speedFunctionConstant1 = new ConstantFunctionBaseline(-30, 0.85);
	private final ConstantFunctionBaseline speedFunctionConstant2 = new ConstantFunctionBaseline(-15, 0.925);
	private final ConstantFunctionBaseline speedFunctionConstant3 = new ConstantFunctionBaseline(0, 1);
	private final ConstantFunctionBaseline speedFunctionConstant4 = new ConstantFunctionBaseline(15, 1.075);
	private final ConstantFunctionBaseline speedFunctionConstant5 = new ConstantFunctionBaseline(30, 1.15);

	@Test
	public void testBaseline100() {
		final double baseline = 100;
		final double expectedPlaytime = 60 * 1000;
		
		speedFunctionConstant1.configure(baseline, expectedPlaytime);
		speedFunctionConstant2.configure(baseline, expectedPlaytime);
		speedFunctionConstant3.configure(baseline, expectedPlaytime);
		speedFunctionConstant4.configure(baseline, expectedPlaytime);
		speedFunctionConstant5.configure(baseline, expectedPlaytime);

		Assert.assertEquals(55, speedFunctionConstant1.getValue(0), DELTA);
		Assert.assertEquals(77.5, speedFunctionConstant2.getValue(0), DELTA);
		Assert.assertEquals(100, speedFunctionConstant3.getValue(0), DELTA);
		Assert.assertEquals(122.5, speedFunctionConstant4.getValue(0), DELTA);
		Assert.assertEquals(145, speedFunctionConstant5.getValue(0), DELTA);
	}
	
	@Test
	public void testBaseline241() {
		final double baseline = 241;
		final double expectedPlaytime = 60 * 1000;
		
		speedFunctionConstant1.configure(baseline, expectedPlaytime);
		speedFunctionConstant2.configure(baseline, expectedPlaytime);
		speedFunctionConstant3.configure(baseline, expectedPlaytime);
		speedFunctionConstant4.configure(baseline, expectedPlaytime);
		speedFunctionConstant5.configure(baseline, expectedPlaytime);

		Assert.assertEquals(174.85, speedFunctionConstant1.getValue(0), DELTA);
		Assert.assertEquals(207.925, speedFunctionConstant2.getValue(0), DELTA);
		Assert.assertEquals(241, speedFunctionConstant3.getValue(0), DELTA);
		Assert.assertEquals(274.075, speedFunctionConstant4.getValue(0), DELTA);
		Assert.assertEquals(307.15, speedFunctionConstant5.getValue(0), DELTA);
	}

}
