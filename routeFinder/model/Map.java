package routeFinder.model;

import java.util.*;

/*
 * Original implementation by Gary Dahl
 */
public class Map {
	
	private final List<Street> streets;
	
	public Map(List<MapPoint> points, List<Street> streets, Bounds bounds) {
		this.streets = streets;
	}
	
	/*
	 * Returns a read only list of streets in this map.
	 */
	public List<Street> streets() {
		return Collections.unmodifiableList(streets);
	}
	
	public MapPoint closestPointTo(MapPoint originalPoint) {
		MapPoint closestPoint = null;
		float closestDistance = Float.MAX_VALUE;
		for (Street thisStreet : streets) {
			MapPoint closestPointOnStreet = closestPointOnStreetTo(originalPoint, thisStreet);
			if(distanceBetween(closestPointOnStreet, originalPoint) < closestDistance) {
				closestPoint = closestPointOnStreet;
				closestDistance = distanceBetween(closestPointOnStreet, originalPoint);
			}
		}
		return closestPoint;
	}
	
	private MapPoint closestPointOnStreetTo(MapPoint originalPoint, Street street) {
		MapPoint closestPoint = null;
		float closestDistance = Float.MAX_VALUE;
		for (MapPoint thisPoint : street.points) {
			if (distanceBetween(thisPoint, originalPoint) < closestDistance) {
				closestPoint = thisPoint;
				closestDistance = distanceBetween(thisPoint, originalPoint);
			}
		}
		return closestPoint;
	}
	
	// Returns the euclidean distance squared between two points.
	private float distanceBetween(MapPoint point1, MapPoint point2) {
		return ( point1.x - point2.x ) * ( point1.x - point2.x )
				+ ( point1.y - point2.y ) * ( point1.y - point2.y );
	}
}
