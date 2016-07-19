package tests;

import static org.junit.Assert.*;

import routeFinder.control.SearchRunner;
import routeFinder.model.MapPoint;
import processing.core.PApplet;

public class ApplicationRunner {

	private SearchRunner main;
	
	public void start() {
		main = new SearchRunner();
	}
	
	public void openMap(String mapFileName) {
		PApplet p = new PApplet();
		main.openMap(p.loadXML(mapFileName));
	}
	
	public void setPoints(MapPoint start, MapPoint end) { 
		main.setFrom(start);
		main.setTo(end);
	}
	
	public void search(int searchType) {
		
	}
	
	public void showsRoute() {
		assertNotNull(main.route());
	}
}
