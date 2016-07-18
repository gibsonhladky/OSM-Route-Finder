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
	Point end = new Point(10, 0);
	
	List<Point> noRoute;
	List<Point> shortRoute;
	List<Point> longCircuitousRoute;
	List<Point> longJaggedRoute;
	List<Point> deadEndRoute;

	@Before
	public void setUp() throws Exception {
		search = new AStarSearch(start, end, 2);
		initializeRoutes();
	}

	@Test
	public void disconnectedMap() {
		givenMapContainingRoute(noRoute);
		
		whenSearched();
		
		thenRouteShouldBe(noRoute);
	}
	
	@Test
	public void nonBranchingMap() {
		givenMapContainingRoute(shortRoute);
		
		whenSearched();
		
		thenRouteShouldBe(shortRoute);
	}
	
	@Test
	public void branchingMap() {
		givenMapContainingRoute(shortRoute);
		givenMapContainingRoute(longCircuitousRoute);
		givenMapContainingRoute(longJaggedRoute);
		
		whenSearched();
		
		thenRouteShouldBe(shortRoute);
	}
	
	@Test
	public void deadEndMap() {
		givenMapContainingRoute(deadEndRoute);
		givenMapContainingRoute(longCircuitousRoute);
		
		whenSearched();
		
		thenRouteShouldBe(longCircuitousRoute);
	}
	
	private void givenMapContainingRoute(List<Point> route) {
		if(!route.isEmpty()) {
			for(int i = 0; i < route.size() - 1; i++) {
				setAsNeighbors(route.get(i), route.get(i + 1));
			}
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
	
	
	private void initializeRoutes() {
		initializeNoRoute();
		initializeShortRoute();
		initializeLongCircuitousRoute();
		initializeLongJaggedRoute();
		initializeDeadEndRoute();
	}
	
	private void initializeNoRoute() {
		noRoute = Collections.emptyList();
	}
	
	private void initializeShortRoute() {
		shortRoute = Arrays.asList(start, end);
	}
	
	private void initializeLongCircuitousRoute() {
		longCircuitousRoute = Arrays.asList(start, new Point(5, 5), end);
	}
	
	private void initializeLongJaggedRoute() {
		longJaggedRoute = new ArrayList<Point>(10);
		longJaggedRoute.add(start);
		for(int i = 1; i < 10; i++) {
			longJaggedRoute.add(new Point(i, i%2));
		}
		longJaggedRoute.add(end);
	}
	
	private void initializeDeadEndRoute() {
		deadEndRoute = Arrays.asList(start, new Point(0, 5));
	}
	

}
