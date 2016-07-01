package routeFinder.view;

import java.util.ArrayList;

import processing.core.PApplet;
import routeFinder.model.Map;
import routeFinder.model.Point;
import routeFinder.model.Street;

public class MapView {

	private PApplet applet;
	private Map map;
	
	private final int height, width;

	public MapView(Map map, PApplet p) {
		this.map = map;
		applet = p;
		height = applet.height;
		width = applet.width;
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
	 * Scales the longitude to fit the screen.
	 */
	private float scaleLon(float lon) {
		return width * ( lon - map.bounds().minLon ) / map.bounds().lonRange;
	}
	
	/*
	 * Scales the latitude to fit the screen.
	 */
	private float scaleLat(float lat) {
		return height
				- ( map.usableHeight * ( lat - map.bounds().minLat ) / map.bounds().latRange ) - ( height - map.usableHeight ) / 2;
	}

	/*
	 * Draws the route found on the map.
	 */
	public void drawRoute(ArrayList<Point> routePoints) {
		applet.stroke(255); // draw white lines between points
		applet.strokeWeight(2);
		for (int i = 1; i < routePoints.size(); i++) {
			drawLineBetween(routePoints.get(i - 1), routePoints.get(i));
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
