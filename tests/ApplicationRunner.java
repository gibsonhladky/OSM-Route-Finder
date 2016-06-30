package tests;

import static org.junit.Assert.*;

import routeFinder.Main;
import routeFinder.Map;
import routeFinder.Point;

public class ApplicationRunner {

	private Main main;
	public Map SIMPLE_MAP;
	
	public void start() {
		main = new Main();
	}
	
	public void openMap(String mapFileName) {
		main.openMap(mapFileName);
	}
	
	public void setPoints(Point start, Point end) { 
		main.setFrom(start);
		main.setTo(end);
	}
	
	public void showsRoute() {
		assertNotNull(main.route());
	}
}
