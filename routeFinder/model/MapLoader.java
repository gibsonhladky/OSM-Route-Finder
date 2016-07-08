package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;

import processing.data.XML;

/*
 * MapLoader loads data from an XML file and interprets the
 * data. The loader can then be queried for information
 * about the map.
 */
public class MapLoader {
	
	private XML mapData;
	private Bounds bounds;
	private final Hashtable<Long, Point> pointIDTable = new Hashtable<Long, Point>();
	private ArrayList<Point> points;
	private ArrayList<Street> streets;
	
	private final int width, height;
	
	public MapLoader(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Map loadMap(XML mapData) {
		this.mapData = mapData;
		load();
		return new Map(points, streets, bounds, width, height);
	}
	
	private void load() {
		loadBounds();
		loadPoints();
		loadStreets();
		removeUnusedPoints();
	}
	
	private void loadBounds() {
		XML boundsData = mapData.getChild("bounds");
		bounds = new Bounds(boundsData.getFloat("minlat"), boundsData.getFloat("minlon"),
				boundsData.getFloat("maxlat"), boundsData.getFloat("maxlon"));
	}
	
	/*
	 * Loads all the points from the XML mapData.
	 */
	private void loadPoints() {
		points = new ArrayList<Point>();
		XML nodes[] = mapData.getChildren("node");
		for (XML node : nodes) {
			if (isInvalidPoint(node)) {
				continue;
			}
			loadPoint(node);
		}
	}
	
	/*
	 * Returns true if the XML node is not a valid point
	 * for a map.
	 */
	private boolean isInvalidPoint(XML node) {
		return !node.hasAttribute("id") || !node.hasAttribute("lat") || !node.hasAttribute("lon");
	}
	
	/*
	 * Loads the point into the loader. Parses the information
	 * from the XML object and adds the Point to 
	 * all necessary collections.
	 */
	private void loadPoint(XML node) {
		long id = node.getLong("id", -1);
		float lat = node.getFloat("lat");
		float lon = node.getFloat("lon");
		Point point = new Point(scaleLon(lon), scaleLat(lat));
		points.add(point);
		pointIDTable.put(id, point);
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
		return height - height * ( lat - bounds.minLat ) / bounds.latRange;
	}
	
	/*
	 * Loads the streets in a map from the XML mapData.
	 * Must be called after loadPoints, as streets are
	 * constructed by connecting Points.
	 */
	private void loadStreets() {
		streets = new ArrayList<Street>();
		
		// Go through each potential street
		XML ways[] = mapData.getChildren("way");
		for (XML road : ways) {
			if (!isRoad(road)) {
				continue;
			}
			
			Street newStreet = createStreet(road);
			streets.add(newStreet);
			setNeighbors(newStreet);
		}
	}
	
	/*
	 * Returns true if the XML object is accessible by
	 * vehicles.
	 */
	private boolean isRoad(XML way) {
		boolean isRoad = false;
		XML tags[] = way.getChildren("tag");
		for (XML tag : tags) {
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
					
				}
			}
		}
		return isRoad;
	}
	
	/*
	 * Creates a Street from the XML object passed in.
	 */
	private Street createStreet(XML road) {
		ArrayList<Point> points = new ArrayList<Point>();
		XML nds[] = road.getChildren("nd");
		for (XML nd : nds) {
			long index = nd.getLong("ref", -1);
			Point nextPoint = pointIDTable.get(index);
			nextPoint.isOnStreet = true;
			points.add(nextPoint);
		}
		
		return new Street(points);
	}
	
	/*
	 * Sets each point in a street as a neighbor to it's
	 * adjacent points.
	 */
	private void setNeighbors(Street street) {
		ArrayList<Point> newPoints = street.points;
		if (newPoints.size() > 1) {
			newPoints.get(0).neighbors.add(newPoints.get(1));
		}
		for (int i = 1; i < newPoints.size() - 1; i++) {
			newPoints.get(i).neighbors.add(newPoints.get(i - 1));
			newPoints.get(i).neighbors.add(newPoints.get(i + 1));
		}
		if (newPoints.size() > 1) {
			newPoints.get(newPoints.size() - 1).neighbors.add(newPoints.get(newPoints.size() - 2));
		}
	}
	
	private void removeUnusedPoints() {
		for (int i = 0; i < points.size(); i++) {
			if (!points.get(i).isOnStreet) {
				points.remove(i);
				i--;
			}
		}
	}
}