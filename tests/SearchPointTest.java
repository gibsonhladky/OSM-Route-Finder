package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import routeFinder.model.MapPoint;
import search.SearchCriteria;
import search.SearchPoint;

public class SearchPointTest {

	MapPoint start = new MapPoint(0, 0);
	MapPoint end = new MapPoint(10, 10);
	
	SearchPoint testPoint;
	SearchPoint searchStart;
	
	SearchCriteria mockCriteria;
	
	@Before
	public void setUp() throws Exception {
		mockCriteria = new SearchCriteria(start, end, 2);
		
		searchStart = new SearchPoint(start, null, mockCriteria);
		testPoint = new SearchPoint(new MapPoint(0, 10), searchStart, mockCriteria);
	}

	@Test
	public void compareToFartherPointsProducesNegative() {
		assertTrue(testPoint.compareTo(fartherPoint()) < 0);
	}
	
	@Test
	public void compareToSameDistanceProducesZero() {
		assertTrue(testPoint.compareTo(sameDistancePoint()) == 0);
	}
	
	@Test
	public void compareToCloserPointProducesPositive() {
		assertTrue(testPoint.compareTo(closerPoint()) > 0);
	}
	
	private SearchPoint fartherPoint() {
		return new SearchPoint(new MapPoint(-10, -10), searchStart, mockCriteria);
	}
	
	private SearchPoint sameDistancePoint() {
		return new SearchPoint(new MapPoint(10, 0), searchStart, mockCriteria);
	}
	
	private SearchPoint closerPoint() {
		return new SearchPoint(new MapPoint(5, 5), searchStart, mockCriteria);
	}

}
