package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import routeFinder.Point;

public class OSMRouteFinderEndToEndTest {
	
	private final ApplicationRunner application = new ApplicationRunner();
	private final Point SIMPLE_MAP_START = new Point(0, 0);
	private final Point SIMPLE_MAP_END = new Point(1, 1);

	@Test
	public void routeFinderReturnsARouteFromStartToEndPoints() {
		application.start();
		application.openMap("SIMPLE_MAP.osm");
		application.setPoints(SIMPLE_MAP_START, SIMPLE_MAP_END);
		application.showsRoute();
	}

}
