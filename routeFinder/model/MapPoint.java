package routeFinder.model;

import java.util.ArrayList;

/*
 * Representation of a point on a map.
 */
public class MapPoint extends Point {
	
	// list of points connected to this one by roads
	public ArrayList<MapPoint> neighbors;
	// temporary variable used to cull unused points
	public boolean isOnStreet;

	public MapPoint(float x, float y) {
		super(x, y);
		neighbors = new ArrayList<MapPoint>();
		isOnStreet = false;
	}

}