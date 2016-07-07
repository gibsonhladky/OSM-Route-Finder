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
	public Point start;
	public Point end;

	// actual points to search between have changed
	public boolean dirtyPoints;
	
	private Bounds bounds;

	int width;
	int height;

	public Map(XML mapData, int width, int height) {
		this.width = width;
		this.height = height;
		initializePointsAndStreets();
		loadMap(mapData);
	}

	public void clear() {
		allPoints.clear();
		allStreets.clear();
	}

	public void moveEndPointsToClosestStreet(Point guiStart, Point guiEnd) {
		float dStart = Float.MAX_VALUE;
		float dEnd = Float.MAX_VALUE;
		float distSqr = Float.MAX_VALUE;
		
		// find closest point to start and end, save locations, and store
		// mapStart & mapEnd
		for (Point point : allPoints) {
			if (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height) {
				continue;
			}

			distSqr = ( guiStart.x - point.x ) * ( guiStart.x - point.x )
					+ ( guiStart.y - point.y ) * ( guiStart.y - point.y );
			if (distSqr < dStart) {
				start = point;
				dStart = distSqr;
			}
			distSqr = ( guiEnd.x - point.x ) * ( guiEnd.x - point.x ) + ( guiEnd.y - point.y ) * ( guiEnd.y - point.y );
			if (distSqr < dEnd) {
				end = point;
				dEnd = distSqr;
			}
		}

		// copy locations of closest points back into
		guiStart.x = start.x;
		guiStart.y = start.y;
		guiEnd.x = end.x;
		guiEnd.y = end.y;

		dirtyPoints = true;
	}
	
	public Bounds bounds() {
		return bounds;
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
