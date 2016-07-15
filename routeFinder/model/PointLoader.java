package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class PointLoader {

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
		NodeList nodes = pointData.getElementsByTagName("node");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (isValidPoint(node)) {
				addPoint(parsePoint(node), parsePointID(node));
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
		
		return attributes.getNamedItem("id") != null ||
				attributes.getNamedItem("lat") != null ||
				attributes.getNamedItem("lon") != null;
	}
	
	private void addPoint(Point newPoint, long id) {
		points.add(newPoint);
		pointReferenceTable.put(id, newPoint);
	}
	
	private Point parsePoint(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		float lon = Float.parseFloat(attributes.getNamedItem("lon").getNodeValue());
		float lat = Float.parseFloat(attributes.getNamedItem("lat").getNodeValue());
		
		return new Point(scaleLon(lon), scaleLat(lat));
	}
	
	private long parsePointID(Node pointNode) {
		NamedNodeMap attributes = pointNode.getAttributes();
		long id = Long.parseLong(attributes.getNamedItem("id").getNodeValue());
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
}
