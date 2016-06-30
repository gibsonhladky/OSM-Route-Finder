package tests;

import org.junit.Test;

import routeFinder.Point;

public class OSMRouteFinderEndToEndTest {
	
	private final ApplicationRunner application = new ApplicationRunner();
	private final Point SIMPLE_MAP_START = new Point(0, 0);
	private final Point SIMPLE_MAP_END = new Point(1, 1);
	private final int ZERO_HEURISTIC = 0;

	@Test
	public void routeFinderReturnsARouteFromStartToEndPoints() throws Exception {
		application.start();
		application.openMap("map.osm");
		application.setPoints(SIMPLE_MAP_START, SIMPLE_MAP_END);
		application.search(ZERO_HEURISTIC);
		application.showsRoute();
	}

}
