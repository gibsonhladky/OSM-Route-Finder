package routeFinder;

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
	// based on aspect ration of map data vs 800x600 window
	public float usableHeight;
	
	private Bounds bounds;

	private int width;
	private int height;

	public Map(XML mapData, int width, int height) {
		this.width = width;
		this.height = height;
		initializePointsAndStreets();
		loadMap(mapData);
	}

	private void loadMap(XML mapData) {
		XML boundsData = mapData.getChild("bounds");
		bounds = new Bounds(boundsData.getFloat("minlat"), boundsData.getFloat("minlon"),
				boundsData.getFloat("maxlat"), boundsData.getFloat("maxlon"));

		usableHeight = ( 1000 * bounds.latRange / bounds.lonRange );
		
		MapLoader loader = new MapLoader(mapData, bounds);
		
		allPoints.addAll(loader.points());
		allStreets.addAll(loader.streets());
		removeUnusedPoints();
	}
	
	private void removeUnusedPoints() {
		ArrayList<Point> remPoints = new ArrayList<Point>();
		for (Point point : allPoints) {
			if (!point.isOnStreet) {
				remPoints.add(point);
			}
		}
		for (Point point : remPoints) {
			allPoints.remove(point);
		}
	}

	private void initializePointsAndStreets() {
		allPoints = new ArrayList<Point>();
		allStreets = new ArrayList<Street>();
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
	
	private class MapLoader {
		
		private XML mapData;
		private final Hashtable<Long, Point> pointIDTable = new Hashtable<Long, Point>();
		private ArrayList<Point> points;
		private ArrayList<Street> streets;
		
		public MapLoader(XML mapData, Bounds bounds) {
			this.mapData = mapData;
			loadPoints(bounds);
			loadStreets();
		}
		
		/*
		 * Loads all the points from the XML mapData.
		 */
		private void loadPoints(Bounds bounds) {
			points = new ArrayList<Point>();
			XML nodes[] = mapData.getChildren("node");
			for (XML node : nodes) {
				// Filter out invalid nodes
				if (!node.hasAttribute("id") || !node.hasAttribute("lat") || !node.hasAttribute("lon")) {
					continue;
				}
				
				long id = node.getLong("id", -1);
				float lat = node.getFloat("lat");
				float lon = node.getFloat("lon");
				Point point = new Point(scaleLon(lon), scaleLat(lat));
				allPoints.add(point);
				pointIDTable.put(id, point);
			}
		}
		
		/*
		 * Scales the longitude to fit the screen.
		 */
		private float scaleLon(float lon) {
			return width * ( lon - bounds.minLon ) / bounds.lonRange;
		}
		
		/*
		 * Scales the latitude to fit the screen.
		 */
		private float scaleLat(float lat) {
			return height
					- ( usableHeight * ( lat - bounds.minLat ) / bounds.latRange ) - ( height - usableHeight ) / 2;
		}
		
		/*
		 * Loads the streets in a map from the XML mapData.
		 * Must be called after loadPoints, as streets are
		 * constructed by connecting Points.
		 */
		private void loadStreets() {
			streets = new ArrayList<Street>();
			XML ways[] = mapData.getChildren("way");
			for (XML way : ways) {
				// read road type and name
				boolean isRoad = false;
				String name = "";
				XML tags[] = way.getChildren("tag");
				for (XML tag : tags)
					if (tag.hasAttribute("k") && tag.hasAttribute("v")) {
						if (tag.getString("k").equals("highway")) {
							switch (tag.getString("v")) {
							case "pedestrian":
							case "footway":
							case "cycleway":
							case "steps":
							case "path":
							case "living street":
								// isRoad = false; // initialized to false above
								break;
							default:
								isRoad = true;
							}
						}
						else if (tag.getString("k").equals("name")) {
							name = tag.getString("v");
						}
					}
				if (!isRoad)
					continue;
				// read list of points
				ArrayList<Point> points = new ArrayList<Point>();
				XML nds[] = way.getChildren("nd");
				for (XML nd : nds) {
					long index = nd.getLong("ref", -1);
					Point nextPoint = pointIDTable.get(index);
					nextPoint.isOnStreet = true;
					points.add(nextPoint);
				}
				// create new street
				Street street = new Street(points, name);
				allStreets.add(street);
				// add neighboring nodes to each node
				if (points.size() > 1) {
					points.get(0).neighbors.add(points.get(1));
				}
				for (int i = 1; i < points.size() - 1; i++) {
					points.get(i).neighbors.add(points.get(i - 1));
					points.get(i).neighbors.add(points.get(i + 1));
				}
				if (points.size() > 1) {
					points.get(points.size() - 1).neighbors.add(points.get(points.size() - 2));
				}
			}
		}
		
		/*
		 * Returns the points loaded from the XML map data.
		 */
		public ArrayList<Point> points() {
			return points;
		}
		
		/*
		 * Returns the streets loaded from the XML map data.
		 */
		public ArrayList<Street> streets() {
			return streets;
		}
	}
}
