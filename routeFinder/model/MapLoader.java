package routeFinder.model;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * MapLoader loads data from a Document and interprets the
 * data.
 */
public class MapLoader {
	
	private static final String MAX_LONGITUDE_TAG = "maxlon";
	private static final String MAX_LATITUDE_TAG = "maxlat";
	private static final String MIN_LONGITUDE_TAG = "minlon";
	private static final String MIN_LATITUDE_TAG = "minlat";
	private static final String BOUNDS_TAG = "bounds";
	
	private Document mapData;
	private Bounds bounds;
	private PointLoader pointLoader;
	private StreetLoader streetLoader;
	
	private final int width, height;
	
	public MapLoader(int width, int height) {
		this.width = width;
		this.height = height;
		
	}
	
	public Map loadMap(Document mapData) {
		this.mapData = mapData;
		load();
		return new Map(pointLoader.getPoints(), streetLoader.getStreets(), bounds);
	}
	
	private void load() {
		loadBounds();
		loadPoints();
		loadStreets();
		removeUnusedPoints();
	}
	
	private void loadBounds() {
		NodeList boundsNodes = mapData.getElementsByTagName(BOUNDS_TAG);
		Node boundsTag = boundsNodes.item(0);
		NamedNodeMap attributes = boundsTag.getAttributes();
		
		float minlat = Float.parseFloat(attributes.getNamedItem(MIN_LATITUDE_TAG).getNodeValue());
		float minlon = Float.parseFloat(attributes.getNamedItem(MIN_LONGITUDE_TAG).getNodeValue());
		float maxlat = Float.parseFloat(attributes.getNamedItem(MAX_LATITUDE_TAG).getNodeValue());
		float maxlon = Float.parseFloat(attributes.getNamedItem(MAX_LONGITUDE_TAG).getNodeValue());
		
		bounds = new Bounds(minlat, minlon, maxlat, maxlon);
	}
	
	private void loadPoints() {
		pointLoader = new PointLoader(width, height, bounds);
		pointLoader.load(mapData);
	}
	
	/*
	 * Must be called after loadPoints, as streets are
	 * constructed by connecting Points.
	 */
	private void loadStreets() {
		streetLoader = new StreetLoader(pointLoader.getPointReferences());
		streetLoader.load(mapData);
	}
	
	private void removeUnusedPoints() {
		pointLoader.removeUnusedPoints();
	}
}