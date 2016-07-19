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
	
	public MapPoint closestPointTo(Point originalPoint) {
		MapPoint closestPoint = null;
		double closestDistance = Float.MAX_VALUE;
		for (Street thisStreet : streets) {
			MapPoint closestPointOnStreet = closestPointOnStreetTo(originalPoint, thisStreet);
			if(closestPointOnStreet.distanceTo(originalPoint) < closestDistance) {
				closestPoint = closestPointOnStreet;
				closestDistance = closestPointOnStreet.distanceTo(originalPoint);
			}
		}
		return closestPoint;
	}
	
	private MapPoint closestPointOnStreetTo(Point originalPoint, Street street) {
		MapPoint closestPoint = null;
		double closestDistance = Float.MAX_VALUE;
		for (MapPoint thisPoint : street.points) {
			if (thisPoint.distanceTo(originalPoint) < closestDistance) {
				closestPoint = thisPoint;
				closestDistance = thisPoint.distanceTo(originalPoint);
			}
		}
		return closestPoint;
	}
}
