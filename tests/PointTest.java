package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import routeFinder.model.Point;

public class PointTest {
	
	private final double ZERO = 0;
	private final double FIVE = 5;
	private final double DELTA = 0.0001;

	Point testPoint = new Point(0, 0);
	
	@Before
	public void setUp() throws Exception {
	}

	@Test public void equalsReturnsFalseOnDifferentPoint() {
		assertFalse(testPoint.equals(differentPoint()));
	}
	
	@Test public void equalsReturnsTrueOnSamePoint() {
		assertTrue(testPoint.equals(testPoint));
	}
	
	@Test public void equalsReturnsTrueOnCopy() {
		assertTrue(testPoint.equals(copy(testPoint)));
	}
	
	@Test public void distanceToSamePointReturns0() {
		assertEquals(ZERO, testPoint.distanceTo(testPoint), DELTA);
	}
	
	@Test public void distanceToDifferentPointReturnsCorrectly() {
		assertEquals(FIVE, testPoint.distanceTo(differentPoint()), DELTA);
	}
	
	private Point differentPoint() {
		return new Point(testPoint.getX()+3, testPoint.getY()+4);
	}
	
	private Point copy(Point original) {
		return new Point(original.getX(), original.getY());
	}

}
