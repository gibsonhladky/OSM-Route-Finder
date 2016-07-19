package routeFinder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Representation of a point on a map.
 */
public class MapPoint extends Point {
	
	// list of points connected to this one by roads
	private List<MapPoint> neighbors;
	// temporary variable used to cull unused points
	public boolean isOnStreet;

	public MapPoint(float x, float y) {
		super(x, y);
		neighbors = new ArrayList<MapPoint>();
		isOnStreet = false;
	}
	
	public List<MapPoint> getNeighbors() {
		return Collections.unmodifiableList(neighbors);
	}
	
	public void addNeighbor(MapPoint newNeighbor) {
		neighbors.add(newNeighbor);
	}

}