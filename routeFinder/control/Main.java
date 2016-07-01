package routeFinder.control;

import processing.core.PApplet;
import processing.data.XML;
import routeFinder.model.Map;
import routeFinder.model.Point;

public class Main {

	public Map map;
	
	private Point start, end;
	private PApplet applet;

	private Point guiDragging;
	public Point guiStart;
	public Point guiEnd;
	
	public Main(PApplet applet) {
		this.applet = applet;
	}
	
	/*
	 * Sets the start and end GUI points to an initial state. The points are set
	 * apart to allow easier usability.
	 */
	public void initializeGuiPoints() {
		guiStart = new Point(applet.width * 2 / 10, applet.height / 2);
		guiEnd = new Point(applet.width * 8 / 10, applet.height / 2);
		guiDragging = null;
		moveEndPointsToClosestStreet();
	}
	
	private void moveEndPointsToClosestStreet() {
		map.moveEndPointsToClosestStreet(guiStart, guiEnd);
	}
	
	public void openMap(XML mapData, int width, int height) {
		map = new Map(mapData, width, height);
		start = new Point(width * 2 / 10, height / 2);
		end = new Point(width * 8 / 10, height / 2);
	}
	
	public void setFrom(Point from) {
		start = from;
		map.moveEndPointsToClosestStreet(start, end);
	}
	
	public void setTo(Point to) {
		end = to;
		map.moveEndPointsToClosestStreet(start, end);
	}
	
	public Object route() {
		return null;
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
	}
	
	/*
	 * Square function. Returns x^2
	 */
	private double sqr(double x) {
		return Math.pow(x, 2);
	}
}
