package routeFinder.model;

import java.util.*;

import processing.data.*;

/*
 * Original implementation by Gary Dahl
 */
public class Map {
	// access to all points and streets
	public ArrayList<Point> allPoints;
	public ArrayList<Street> allStreets;
	// actual points to search between
	private Point start;
	private Point end;
	
	private Bounds bounds;

	int width;
	int height;

	public Map(XML mapData, int width, int height) {
		this.width = width;
		this.height = height;
		initializePointsAndStreets();
		loadMap(mapData);
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
	
	private void moveToClosestPoint(Point pointToMove) {
		Point closestPoint = null;
		double distanceToClosestPoint = Double.MAX_VALUE;
		for (Point point : allPoints) {
			double distanceToThisPoint = ( pointToMove.x - point.x ) * ( pointToMove.x - point.x )
					+ ( pointToMove.y - point.y ) * ( pointToMove.y - point.y );
			if (distanceToThisPoint < distanceToClosestPoint) {
				closestPoint = point;
				distanceToClosestPoint = distanceToThisPoint;
			}
		}
		pointToMove = closestPoint;
	}

	private void loadMap(XML mapData) {
		MapLoader loader = new MapLoader(this, mapData);
		
		bounds = loader.bounds();
		allPoints.addAll(loader.points());
		allStreets.addAll(loader.streets());
		removeUnusedPoints();
	}
	
	private void removeUnusedPoints() {
		for (int i = 0; i < allPoints.size(); i++) {
			if (!allPoints.get(i).isOnStreet) {
				allPoints.remove(i);
				i--;
			}
		}
	}

	private void initializePointsAndStreets() {
		allPoints = new ArrayList<Point>();
		allStreets = new ArrayList<Street>();
	}

	public class Bounds {
		public final float minLat;
		public final float minLon;
		public final float maxLat;
		public final float maxLon;
		public final float latRange;
		public final float lonRange;

		public Bounds(float minLat, float minLon, float maxLat, float maxLon) {
			this.minLat = minLat;
			this.minLon = minLon;
			this.maxLat = maxLat;
			this.maxLon = maxLon;
			this.latRange = maxLat - minLat;
			this.lonRange = maxLon - minLon;
		}
	}
}
