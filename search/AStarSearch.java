package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import routeFinder.model.MapPoint;

/*
 * Original structure provided by Gary Dahl
 */
public class AStarSearch {

	SearchCriteria criteria;
	
	private SearchPoint startPoint, goalPoint;
	private ArrayList<SearchPoint> frontier;
	private ArrayList<SearchPoint> explored;

	
	public AStarSearch(MapPoint start, MapPoint end, int heuristic) {
		criteria = new SearchCriteria(start, end, heuristic);
		this.startPoint = new SearchPoint(start, null, criteria);
		this.goalPoint = new SearchPoint(end, null, criteria);
		frontier = new ArrayList<SearchPoint>();
		explored = new ArrayList<SearchPoint>();
		
		// Load the frontier with the start point
		frontier.add(startPoint);
	}
	
	public void search() {
		while (!isComplete()) {
			SearchPoint nextPoint = getBestPointInFrontier();
			addPointToExplored(nextPoint);
			updateFrontierWithNeighborsOf(nextPoint);
		}
	}
	
	public List<MapPoint> getRoute() {
		if (goalReached()) {
			List<MapPoint> route = traceBackRouteFrom(goalPoint);
			Collections.reverse(route);
			return route;
		}
		else {
			return Collections.emptyList();
		}
	}

	private boolean isComplete() {
		return goalReached() || noRouteExists();
	}
	
	private boolean goalReached() {
		for(SearchPoint p : explored) {
			if (p.equals(goalPoint)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean noRouteExists() {
		return frontier.isEmpty();
	}
	
	private SearchPoint getBestPointInFrontier() {
		Collections.sort(frontier);
		
		SearchPoint bestPoint = frontier.remove(0);
		explored.add(bestPoint);
		
		return bestPoint;
	}
	
	private void addPointToExplored(SearchPoint exploredPoint) {
		explored.add(exploredPoint);
	}

	
	private List<MapPoint> traceBackRouteFrom(SearchPoint thisPoint) {
		ArrayList<MapPoint> reverseRoute = new ArrayList<MapPoint>();
		SearchPoint currPoint = thisPoint;
		while (currPoint != null) {
			reverseRoute.add(currPoint);
			currPoint = currPoint.previous();
		}
		return reverseRoute;
	}
	
	/*
	 * Adds the neighbors of the point parameter to the frontier
	 * to be searched later. If a point already in the frontier 
	 * can be reached faster through this point, it is updated.
	 */
	private void updateFrontierWithNeighborsOf(SearchPoint point) {
		List<MapPoint> neighbors = point.getNeighbors();
		for (MapPoint neighbor : neighbors) {
			SearchPoint newPoint = createSearchPoint(neighbor, point);
			if (isNewPoint(newPoint)) {
				frontier.add(newPoint);
			}
			if (frontier.contains(newPoint)) {
				updateFrontierWithBestPoint(newPoint);
			}
		}
	}
	
	private SearchPoint createSearchPoint(MapPoint mapPoint, SearchPoint prev) {
		return new SearchPoint(mapPoint, prev, criteria);
	}
	
	private boolean isNewPoint(SearchPoint point) {
		return !frontier.contains(point) && !explored.contains(point);
	}
	
	/*
	 * Replaces the point in the frontier matching this replacement
	 * if the replacement is a better point for the search.
	 */
	private void updateFrontierWithBestPoint(SearchPoint replacement) {
		for (SearchPoint existingPoint : frontier) {
			if (isValidReplacementFor(replacement, existingPoint)) {
				existingPoint = replacement;
			}
			
			// TODO: Find a better way to handle updating goalPoint's
			// traceback pointer.
			
			// update goalPoint for a traceback pointer.
			if(replacement.equals(goalPoint)) {
				goalPoint = replacement;
			}
		}
	}
	
	private boolean isValidReplacementFor(SearchPoint replacement, SearchPoint original) {
		return original.equals(replacement) && original.compareTo(replacement) > 0;
	}
}
