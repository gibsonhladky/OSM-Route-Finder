package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import processing.data.XML;

/*
 * MapLoader loads data from an XML file and interprets the
 * data. The loader can then be queried for information
 * about the map.
 */
public class MapLoader {
	
	private Document mapData;
	private Bounds bounds;
	private final Hashtable<Long, Point> pointIDTable = new Hashtable<Long, Point>();
	private ArrayList<Point> points;
	private ArrayList<Street> streets;
	
	private final int width, height;
	
	public MapLoader(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Map loadMap(Document mapData) {
		this.mapData = mapData;
		load();
		return new Map(points, streets, bounds);
	}
	
	private void load() {
		loadBounds();
		loadPoints();
		loadStreets();
		removeUnusedPoints();
	}
	
	private void loadBounds() {
		NodeList boundsNodes = mapData.getElementsByTagName("bounds");
		Node boundsTag = boundsNodes.item(0);
		NamedNodeMap attributes = boundsTag.getAttributes();
		
		float minlat = Float.parseFloat(attributes.getNamedItem("minlat").getNodeValue());
		float minlon = Float.parseFloat(attributes.getNamedItem("minlon").getNodeValue());
		float maxlat = Float.parseFloat(attributes.getNamedItem("maxlat").getNodeValue());
		float maxlon = Float.parseFloat(attributes.getNamedItem("maxlon").getNodeValue());
		
		bounds = new Bounds(minlat, minlon, maxlat, maxlon);
	}
	
	/*
	 * Loads all the points from the XML mapData.
	 */
	private void loadPoints() {
		points = new ArrayList<Point>();
		NodeList nodes = mapData.getElementsByTagName("node");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
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
	private boolean isInvalidPoint(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		
		return attributes.getNamedItem("id") == null ||
				attributes.getNamedItem("lat") == null ||
				attributes.getNamedItem("lon") == null;
	}
	
	/*
	 * Loads the point into the loader. Parses the information
	 * from the XML object and adds the Point to 
	 * all necessary collections.
	 */
	private void loadPoint(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		long id = Long.parseLong(attributes.getNamedItem("id").getNodeValue());
		float lat = Float.parseFloat(attributes.getNamedItem("lat").getNodeValue());
		float lon = Float.parseFloat(attributes.getNamedItem("lon").getNodeValue());
		
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
		NodeList ways = mapData.getElementsByTagName("way");
		for (int i = 0; i < ways.getLength(); i++) {
			Node road = ways.item(i);
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
	private boolean isRoad(Node way) {
		boolean isRoad = false;
		NodeList tags = way.getChildNodes();
		
		for (int i = 0; i < tags.getLength(); i++) {
			Node tag = tags.item(i);
			if(tag.getNodeName() == null || !tag.getNodeName().equals("tag")) {
				continue;
			}
			
			NamedNodeMap attributes = tag.getAttributes();
			Node k = attributes.getNamedItem("k");
			Node v = attributes.getNamedItem("v");
			if (k != null && v != null) {
				if (k.getNodeValue().equals("highway")) {
					switch (v.getNodeValue()) {
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
			}
		}
		return isRoad;
	}
	
	/*
	 * Creates a Street from the XML object passed in.
	 */
	private Street createStreet(Node road) {
		ArrayList<Point> points = new ArrayList<Point>();
		
		NodeList nds = road.getChildNodes();
		
		for (int i = 0; i < nds.getLength(); i++) {
			Node nd = nds.item(i);
			if(!nd.getNodeName().equals("nd")) {
				continue;
			}
			
			NamedNodeMap attributes = nd.getAttributes();
			long index = Long.parseLong(attributes.getNamedItem("ref").getNodeValue());
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