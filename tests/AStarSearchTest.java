package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import routeFinder.model.AStarSearch;
import routeFinder.model.Point;

public class AStarSearchTest {
	
	AStarSearch search;
	
	Point start = new Point(0, 0);
	Point end = new Point(0, 1);

	@Before
	public void setUp() throws Exception {
		search = new AStarSearch(start, end, 2);
	}

	@Test
	public void disconnectedMapResultInNoRoute() {
		givenMapContainingRoute(noRoute());
		
		whenSearched();
		
		thenRouteShouldBe(Collections.emptyList());
	}
	
	@Test
	public void nonBranchingMapResultsInProperRoute() {
		givenMapContainingRoute(shortRoute());
		
		whenSearched();
		
		thenRouteShouldBe(shortRoute());
	}
	
	@Test
	public void branchingMapResultsInProperRoute() {
		givenMapContainingRoute(shortRoute());
		givenMapContainingRoute(longRoute());
		
		whenSearched();
		
		thenRouteShouldBe(shortRoute());
	}
	
	private void givenMapContainingRoute(List<Point> route) {
		if(route.isEmpty()) {
			return;
		}
		
		for(int i = 0; i < route.size() - 1; i++) {
			setAsNeighbors(route.get(i), route.get(i + 1));
		}
	}
	
	private void setAsNeighbors(Point p1, Point p2) {
		p1.neighbors.add(p2);
		p2.neighbors.add(p1);
	}
	
	private void whenSearched() {
		search.search();
	}
	
	private void thenRouteShouldBe(List<Point> expectedRoute) {
		assertEquals(expectedRoute, search.getRoute());
	}
	
	private List<Point> noRoute() {
		return Collections.emptyList();
	}
	
	private List<Point> shortRoute() {
		return Arrays.asList(start, end);
	}
	
	private List<Point> longRoute() {
		return Arrays.asList(start, new Point(10, 10), end);
	}

}
