package routeFinder;

import java.util.ArrayList;

import processing.core.PApplet;

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
			for (int i = 1; i < street.points.size(); i++) {
				applet.line(street.points.get(i - 1).x, street.points.get(i - 1).y, street.points.get(i).x,
						street.points.get(i).y);
			}
		}
	}

	/*
	 * Draws the route found on the map.
	 */
	public void drawRoute(ArrayList<Point> routePoints) {
		applet.stroke(255); // draw white lines between points
		applet.strokeWeight(2);
		for (int i = 1; i < routePoints.size(); i++) {
			applet.line(routePoints.get(i - 1).x, routePoints.get(i - 1).y, routePoints.get(i).x,
					routePoints.get(i).y);
		}
	}

	/*
	 * Updates the position of the start and end points.
	 * Allows the user to drag the points to new positions.
	 * Draws the points after being called.
	 */
	public void updateStartAndEndPoints() {
		// check for mouse press over start and end locations
		if (applet.mousePressed && map.guiDragging == null) {
			float dToStartSqr = (float) ( sqr(applet.mouseX - map.guiStart.x) + sqr(applet.mouseY - map.guiStart.y) );
			float dToEndSqr = (float) ( sqr(applet.mouseX - map.guiEnd.x) + sqr(applet.mouseY - map.guiEnd.y) );
			if (dToStartSqr <= 50 && dToStartSqr < dToEndSqr) {
				map.guiDragging = map.guiStart;
			}
			else if (dToEndSqr <= 50) {
				map.guiDragging = map.guiEnd;
			}
		}
		// dragging start or end location
		if (applet.mousePressed & ( map.guiDragging != null )) {
			map.guiDragging.x = applet.mouseX;
			map.guiDragging.y = applet.mouseY;
		}
		// stop dragging start or end location
		if (!applet.mousePressed && ( map.guiDragging != null )) {
			map.moveEndPointsToClosestStreet();
			map.guiDragging = null;
		}

		drawStartAndEnd();
	}

	/*
	 * Draws the start and end points on the map.
	 */
	public void drawStartAndEnd() {
		applet.fill(0, 0, 0, 0);
		applet.stroke(0, 255, 0);
		applet.ellipse(map.guiStart.x, map.guiStart.y, 8, 8);
		applet.stroke(255, 0, 0);
		applet.ellipse(map.guiEnd.x, map.guiEnd.y, 8, 8);
	}
	
	/*
	 * Square function. Returns x^2
	 */
	private double sqr(double x) {
		return Math.pow(x, 2);
	}

}
