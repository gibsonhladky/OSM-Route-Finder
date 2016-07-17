package routeFinder.model;

import java.util.*;

/*
 * Original implementation by Gary Dahl
 */
public class Map {
	// access to all points and streets
	private List<Point> allPoints;
	private List<Street> allStreets;
	// actual points to search between
	private Point start;
	private Point end;
	
	private Bounds bounds;
	
	public Map(List<Point> points, List<Street> streets, Bounds bounds) {
		this.allPoints = points;
		this.allStreets = streets;
		start = new Point(0, 0);
		end = new Point(0, 0);
	}
	
	/*
	 * Sets the start point on the map. If the point is not
	 * already on the map, the closest point to it will be set
	 * instead.
	 */
	public void setStartPoint(Point newStart) {
		this.start = closestPointTo(newStart);
	}
	
	/*
	 * Sets the end point on the map. If the point is not
	 * already on the map, the closest point to it will be set
	 * instead.
	 */
	public void setEndPoint(Point newEnd) {
		this.end = closestPointTo(newEnd);
	}
	
	public Point startPoint() {
		return start;
	}
	
	public Point endPoint() {
		return end;
	}
	
	public List<Street> streets() {
		return allStreets;
	}

	public void clear() {
		allPoints.clear();
		allStreets.clear();
	}

	public void movePointToClosestStreet(Point guiStart, Point guiEnd) {
		start = closestPointTo(guiStart);
		end = closestPointTo(guiEnd);

		// copy locations of closest points back into
		guiStart.x = start.x;
		guiStart.y = start.y;
		guiEnd.x = end.x;
		guiEnd.y = end.y;
	}
	
	public Bounds bounds() {
		return bounds;
	}
	
	private Point closestPointTo(Point originalPoint) {
		Point closestPoint = null;
		float closestDistance = Float.MAX_VALUE;
		for (Point thisPoint : allPoints) {
			if (distanceBetween(thisPoint, originalPoint) < closestDistance) {
				closestPoint = thisPoint;
				closestDistance = distanceBetween(thisPoint, originalPoint);
			}
		}
		return closestPoint;
	}
	
	private float distanceBetween(Point point1, Point point2) {
		return ( point1.x - point2.x ) * ( point1.x - point2.x )
				+ ( point1.y - point2.y ) * ( point1.y - point2.y );
	}
}
