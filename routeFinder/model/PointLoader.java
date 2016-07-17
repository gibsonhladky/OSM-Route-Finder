package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class PointLoader {

	private static final String LONGITUDE_TAG = "lon";
	private static final String LATITUDE_TAG = "lat";
	private static final String ID_TAG = "id";
	
	private List<Point> points;
	private final Hashtable<Long, Point> pointReferenceTable = new Hashtable<Long, Point>();
	private Bounds bounds;
	
	private final int width, height;
	
	public PointLoader(int width, int height, Bounds bounds) {
		this.width = width;
		this.height = height;
		this.bounds = bounds;
		
		points = new ArrayList<Point>();
	}
	
	public void load(Document pointData) {
		NodeList pointNodes = pointData.getElementsByTagName("node");
		for (Node pointNode : asList(pointNodes)) {
			if (isValidPoint(pointNode)) {
				addPoint(parsePoint(pointNode), parsePointID(pointNode));
			}
		}
	}
	
	public List<Point> getPoints() {		
		return points;
	}
	
	public Hashtable<Long, Point> getPointReferences() {
		return pointReferenceTable;
	}
	
	private boolean isValidPoint(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		
		return attributes.getNamedItem(ID_TAG) != null ||
				attributes.getNamedItem(LATITUDE_TAG) != null ||
				attributes.getNamedItem(LONGITUDE_TAG) != null;
	}
	
	private void addPoint(Point newPoint, long id) {
		points.add(newPoint);
		pointReferenceTable.put(id, newPoint);
	}
	
	private Point parsePoint(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		float lon = Float.parseFloat(attributes.getNamedItem(LONGITUDE_TAG).getNodeValue());
		float lat = Float.parseFloat(attributes.getNamedItem(LATITUDE_TAG).getNodeValue());
		
		return new Point(scaleLon(lon), scaleLat(lat));
	}
	
	private long parsePointID(Node pointNode) {
		NamedNodeMap attributes = pointNode.getAttributes();
		long id = Long.parseLong(attributes.getNamedItem(ID_TAG).getNodeValue());
		return id;
	}
	
	private float scaleLon(float lon) {
		return width * ( lon - bounds.minLon ) / bounds.lonRange;
	}
	
	private float scaleLat(float lat) {
		return height - height * ( lat - bounds.minLat ) / bounds.latRange;
	}
	
	public void removeUnusedPoints() {
		for (int i = 0; i < points.size(); i++) {
			if (!points.get(i).isOnStreet) {
				points.remove(i);
				i--;
			}
		}
	}
	
	/*
	 * Converts a NodeList to a List.
	 * This makes reading the code a heck of a lot easier.
	 */
	// TODO: Move this to a utility class.
	private static List<Node> asList(NodeList nodeList) {
		List<Node> list = new ArrayList<Node>(nodeList.getLength());
		for(int i = 0; i < nodeList.getLength(); i++) {
			list.add(nodeList.item(i));
		}
		return list;
	}
}
