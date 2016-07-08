package routeFinder.model;

import java.util.*;

import processing.data.*;

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

	private int width;
	private int height;
	
	public Map(List<Point> points, List<Street> streets, Bounds bounds, int width, int height) {
		this.allPoints = points;
		this.allStreets = streets;
		this.bounds = bounds;
		this.width = width;
		this.height = height;
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
		float dStart = Float.MAX_VALUE;
		float distSqr = Float.MAX_VALUE;
		for (Point thisPoint : allPoints) {
			if (thisPoint.x < 0 || thisPoint.x >= width || thisPoint.y < 0 || thisPoint.y >= height) {
				continue;
			}
			distSqr = ( originalPoint.x - thisPoint.x ) * ( originalPoint.x - thisPoint.x )
					+ ( originalPoint.y - thisPoint.y ) * ( originalPoint.y - thisPoint.y );
			if (distSqr < dStart) {
				closestPoint = thisPoint;
				dStart = distSqr;
			}
		}
		return closestPoint;
	}
}
