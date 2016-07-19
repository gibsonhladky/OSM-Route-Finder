package tests;

import org.junit.Test;

import routeFinder.model.MapPoint;

public class OSMRouteFinderEndToEndTest {
	
	private final ApplicationRunner application = new ApplicationRunner();
	private final MapPoint SIMPLE_MAP_START = new MapPoint(0, 0);
	private final MapPoint SIMPLE_MAP_END = new MapPoint(1, 1);
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
