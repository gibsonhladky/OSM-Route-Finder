package routeFinder.view;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import routeFinder.model.Map;
import routeFinder.model.Point;
import routeFinder.model.Street;

public class MapView {

	private PApplet applet;
	private Map map;

	public MapView(Map map, PApplet p) {
		this.map = map;
		applet = p;
	}

	/*
	 * Draws the map on the applet.
	 */
	public void drawMap() {
		applet.stroke(127);
		applet.strokeWeight(1);
		for (Street street : map.allStreets) {
			drawStreet(street);
		}
	}
	
	/*
	 * Draws a whole street by connecting the points along the street.
	 */
	private void drawStreet(Street street) {
		for (int i = 1; i < street.points.size(); i++) {
			drawLineBetween(street.points.get(i - 1), street.points.get(i));
		}
	}
	
	/*
	 * Draws a line on the applet between two points.
	 */
	private void drawLineBetween(Point p1, Point p2) {
		applet.line(p1.x, p1.y, p2.x, p2.y);
	}

	/*
	 * Draws the route found on the map.
	 */
	public void drawRoute(List<Point> routePoints) {
		applet.stroke(255); // draw white lines between points
		applet.strokeWeight(2);
		for (int i = 1; i < routePoints.size(); i++) {
			drawLineBetween(routePoints.get(i - 1), routePoints.get(i));
		}
	}
	
	/*
	 * Draws all points to the map.
	 */
	public void drawPoints(ArrayList<Point> points) {
		for (Point p : points) {
			applet.ellipse(p.x, p.y, 4, 4);
		}
	}

	/*
	 * Draws the start and end points on the map.
	 */
	public void drawStartAndEnd(Point guiStart, Point guiEnd) {
		applet.fill(0, 0, 0, 0);
		applet.stroke(0, 255, 0);
		applet.ellipse(guiStart.x, guiStart.y, 8, 8);
		applet.stroke(255, 0, 0);
		applet.ellipse(guiEnd.x, guiEnd.y, 8, 8);
	}
}
