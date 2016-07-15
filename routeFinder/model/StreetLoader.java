package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class StreetLoader {

	private static final String HIGHWAY_TAG = "highway";
	private static final String POINT_REFERENCE_TAG = "ref";
	private static final String POINT_ON_STREET_TAG = "nd";
	private static final String STREET_TAG = "way";
	
	private ArrayList<Street> streets;
	private Hashtable<Long, Point> pointReferenceTable; 
	
	public StreetLoader(Hashtable<Long, Point> pointReferenceTable) {
		this.pointReferenceTable = pointReferenceTable;
	}
	
	public List<Street> getStreets() {
		return streets;
	}
	
	public void load(Document streetData) {
		streets = new ArrayList<Street>();
		
		NodeList streetReferences = streetData.getElementsByTagName(STREET_TAG);
		for (int i = 0; i < streetReferences.getLength(); i++) {
			Node streetReference = streetReferences.item(i);
			if (isValidStreetReference(streetReference)) {
				addStreet(parseStreet(streetReference));	
			}
		}
	}
	
	private boolean isValidStreetReference(Node way) {
		NodeList tags = way.getChildNodes();
		for (int i = 0; i < tags.getLength(); i++) {
			Node tag = tags.item(i);
			if(isValidTag(tag) && isRoadTag(tag)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidTag(Node tag) {
		return !(tag.getNodeName() == null) && 
				(tag.getNodeName().equals("tag"));
	}
	
	private boolean isRoadTag(Node tag) {
		NamedNodeMap attributes = tag.getAttributes();
		// TODO: Research meaning behind k and v and rename these variables
		Node k = attributes.getNamedItem("k");
		Node v = attributes.getNamedItem("v");
		if(k == null || v == null) {
			return false;
		}
		else if(k.getNodeValue().equals(HIGHWAY_TAG)) {
			switch (v.getNodeValue()) {
			case "pedestrian":
			case "footway":
			case "cycleway":
			case "steps":
			case "path":
			case "living street":
				break;
			default:
				return true;
			}
		}
		return false;
	}
	
	private void addStreet(Street newStreet) {
		streets.add(newStreet);
	}
	
	private Street parseStreet(Node road) {
		NodeList pointReferences = road.getChildNodes();	
		ArrayList<Point> streetPoints = parsePointsAlongStreet(pointReferences);
		setNeighbors(streetPoints);
		return new Street(streetPoints);
	}
	
	private ArrayList<Point> parsePointsAlongStreet(NodeList pointReferences) {
		ArrayList<Point> pointsAlongRoad = new ArrayList<Point>();
		for (int i = 0; i < pointReferences.getLength(); i++) {
			Node pointReference = pointReferences.item(i);
			if(isValidPointReference(pointReference)) {
				pointsAlongRoad.add(getReferencedPoint(pointReference));
			}
		}
		return pointsAlongRoad;
	}
	
	private boolean isValidPointReference(Node pointReference) {
		return pointReference.getNodeName().equals(POINT_ON_STREET_TAG)
				&& pointReference.getAttributes().getNamedItem(POINT_REFERENCE_TAG) != null;
	}
	
	/*
	 * Expects pointReference to be valid.
	 */
	private Point getReferencedPoint(Node pointReference) {
		NamedNodeMap attributes = pointReference.getAttributes();
		long referenceID = Long.parseLong(attributes.getNamedItem(POINT_REFERENCE_TAG).getNodeValue());
		Point nextPoint = pointReferenceTable.get(referenceID);
		nextPoint.isOnStreet = true;
		return nextPoint;
	}
	
	private void setNeighbors(ArrayList<Point> streetPoints) {
		if (streetPoints.size() > 1) {
			streetPoints.get(0).neighbors.add(streetPoints.get(1));
		}
		for (int i = 1; i < streetPoints.size() - 1; i++) {
			streetPoints.get(i).neighbors.add(streetPoints.get(i - 1));
			streetPoints.get(i).neighbors.add(streetPoints.get(i + 1));
		}
		if (streetPoints.size() > 1) {
			streetPoints.get(streetPoints.size() - 1).neighbors.add(streetPoints.get(streetPoints.size() - 2));
		}
	}
	
}
