package routeFinder.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class StreetLoader {

	private static final String V_TAG = "v";
	private static final String K_TAG = "k";
	private static final String TAG = "tag";
	private static final String HIGHWAY_TAG = "highway";
	private static final String POINT_REFERENCE_TAG = "ref";
	private static final String POINT_ON_STREET_TAG = "nd";
	private static final String STREET_TAG = "way";
	
	private ArrayList<Street> streets;
	private Hashtable<Long, MapPoint> pointReferenceTable; 
	
	public StreetLoader(Hashtable<Long, MapPoint> pointReferenceTable) {
		this.pointReferenceTable = pointReferenceTable;
	}
	
	public List<Street> getStreets() {
		return streets;
	}
	
	public void load(Document mapData) {
		streets = new ArrayList<Street>();
		NodeList streetReferences = mapData.getElementsByTagName(STREET_TAG);
		for (Node streetReference : asList(streetReferences)) {
			if (isValidStreetReference(streetReference)) {
				addStreet(parseStreet(streetReference));	
			}
		}
	}
	
	private boolean isValidStreetReference(Node way) {
		NodeList tags = way.getChildNodes();
		for (Node tag : asList(tags)) {
			if(isValidTag(tag) && isRoadTag(tag)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidTag(Node tag) {
		return !(tag.getNodeName() == null) && 
				(tag.getNodeName().equals(TAG));
	}
	
	private boolean isRoadTag(Node tag) {
		NamedNodeMap attributes = tag.getAttributes();
		// TODO: Research meaning behind k and v and rename these variables
		Node k = attributes.getNamedItem(K_TAG);
		Node v = attributes.getNamedItem(V_TAG);
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
		ArrayList<MapPoint> streetPoints = parsePointsAlongStreet(pointReferences);
		setNeighbors(streetPoints);
		return new Street(streetPoints);
	}
	
	private ArrayList<MapPoint> parsePointsAlongStreet(NodeList pointReferences) {
		ArrayList<MapPoint> pointsAlongRoad = new ArrayList<MapPoint>();
		for (Node pointReference : asList(pointReferences)) {
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
	private MapPoint getReferencedPoint(Node pointReference) {
		NamedNodeMap attributes = pointReference.getAttributes();
		long referenceID = Long.parseLong(attributes.getNamedItem(POINT_REFERENCE_TAG).getNodeValue());
		MapPoint nextPoint = pointReferenceTable.get(referenceID);
		nextPoint.isOnStreet = true;
		return nextPoint;
	}
	
	private void setNeighbors(ArrayList<MapPoint> streetPoints) {
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
