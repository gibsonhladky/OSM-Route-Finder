package routeFinder;

import java.util.ArrayList;

import processing.core.PApplet;

public class MapView {

	private PApplet applet;
	private Map map;
	
	private Point guiStart;
	private Point guiEnd;
	private Point guiDragging;
	
	private final int height, width;

	public MapView(Map map, PApplet p) {
		this.map = map;
		applet = p;
		height = applet.height;
		width = applet.width;
		initializeGuiPoints();
	}
	
	/*
	 * Sets the start and end GUI points to an initial state. The points are set
	 * apart to allow easier usability.
	 */
	private void initializeGuiPoints() {
		guiStart = new Point(applet.width * 2 / 10, applet.height / 2);
		guiEnd = new Point(applet.width * 8 / 10, applet.height / 2);
		guiDragging = null;
		moveEndPointsToClosestStreet();
	}

	private void moveEndPointsToClosestStreet() {
		map.moveEndPointsToClosestStreet(guiStart, guiEnd);
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
	 * Updates the position of the start and end points.
	 * Allows the user to drag the points to new positions.
	 * Draws the points after being called.
	 */
	public void updateStartAndEndPoints() {
		// check for mouse press over start and end locations
		if (applet.mousePressed && guiDragging == null) {
			float dToStartSqr = (float) ( sqr(applet.mouseX - guiStart.x) + sqr(applet.mouseY - guiStart.y) );
			float dToEndSqr = (float) ( sqr(applet.mouseX - guiEnd.x) + sqr(applet.mouseY - guiEnd.y) );
			if (dToStartSqr <= 50 && dToStartSqr < dToEndSqr) {
				guiDragging = guiStart;
			}
			else if (dToEndSqr <= 50) {
				guiDragging = guiEnd;
			}
		}
		// dragging start or end location
		if (applet.mousePressed & ( guiDragging != null )) {
			guiDragging.x = applet.mouseX;
			guiDragging.y = applet.mouseY;
		}
		// stop dragging start or end location
		if (!applet.mousePressed && ( guiDragging != null )) {
			map.moveEndPointsToClosestStreet(guiStart, guiEnd);
			guiDragging = null;
		}

		drawStartAndEnd();
	}

	/*
	 * Draws the start and end points on the map.
	 */
	public void drawStartAndEnd() {
		applet.fill(0, 0, 0, 0);
		applet.stroke(0, 255, 0);
		applet.ellipse(guiStart.x, guiStart.y, 8, 8);
		applet.stroke(255, 0, 0);
		applet.ellipse(guiEnd.x, guiEnd.y, 8, 8);
	}
	
	/*
	 * Square function. Returns x^2
	 */
	private double sqr(double x) {
		return Math.pow(x, 2);
	}
}
