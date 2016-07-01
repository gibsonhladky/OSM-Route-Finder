package tests;

import static org.junit.Assert.*;

import routeFinder.control.Main;
import routeFinder.model.Point;
import processing.core.PApplet;

public class ApplicationRunner {

	private Main main;
	
	public void start() {
		main = new Main();
	}
	
	public void openMap(String mapFileName) {
		PApplet p = new PApplet();
		main.openMap(p.loadXML(mapFileName));
	}
	
	public void setPoints(Point start, Point end) { 
		main.setFrom(start);
		main.setTo(end);
	}
	
	public void search(int searchType) {
		
	}
	
	public void showsRoute() {
		assertNotNull(main.route());
	}
}
