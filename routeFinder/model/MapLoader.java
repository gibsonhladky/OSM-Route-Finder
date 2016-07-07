package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;

import processing.data.XML;
import routeFinder.model.Map.Bounds;

/*
 * MapLoader loads data from an XML file and interprets the
 * data. The loader can then be queried for information
 * about the map.
 */
class MapLoader {
	
	private final Map mapLoader;
	private XML mapData;
	private Bounds bounds;
	private final Hashtable<Long, Point> pointIDTable = new Hashtable<Long, Point>();
	private ArrayList<Point> points;
	private ArrayList<Street> streets;
	
	private int widthForScale;
	private int heightForScale;
	
	public MapLoader(Map map, XML mapData, int width, int height) {
		mapLoader = map;
		this.mapData = mapData;
		this.widthForScale = width;
		this.heightForScale = height;
		
		loadBounds();
		loadPoints();
		loadStreets();
	}
	
	/*
	 * Returns the bounds loaded from the XML map data.
	 */
	public Bounds bounds() {
		return bounds;
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
	
	private void loadBounds() {
		XML boundsData = mapData.getChild("bounds");
		bounds = mapLoader.new Bounds(boundsData.getFloat("minlat"), boundsData.getFloat("minlon"),
				boundsData.getFloat("maxlat"), boundsData.getFloat("maxlon"));
	}
	
	/*
	 * Loads all the points from the XML mapData.
	 */
	private void loadPoints() {
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
			points.add(point);
			pointIDTable.put(id, point);
		}
	}
	
	/*
	 * Scales the longitude to fit the screen.
	 */
	private float scaleLon(float lon) {
		return widthForScale * ( lon - bounds.minLon ) / bounds.lonRange;
	}
	
	/*
	 * Scales the latitude to fit the screen.
	 */
	private float scaleLat(float lat) {
		return heightForScale - heightForScale * ( lat - bounds.minLat ) / bounds.latRange;
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
			streets.add(street);
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
}