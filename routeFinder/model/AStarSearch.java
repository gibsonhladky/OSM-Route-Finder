package routeFinder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Original structure provided by Gary Dahl
 */
public class AStarSearch {
	private ArrayList<SearchPoint> frontier;
	private ArrayList<SearchPoint> explored;

	// Two SearchPoints to keep track of the start and goal
	public SearchPoint startPoint, goalPoint;

	// The chosen heuristic
	// 0: always estimate zero, 1: manhattan distance, 2: euclidean l2 distance
	public int heuristic;

	public AStarSearch(Map map, int heuristic) {
		this.startPoint = new SearchPoint(map.start, map.end, heuristic, map.start, null);
		this.goalPoint = new SearchPoint(map.start, map.end, heuristic, map.end, null);
		this.heuristic = heuristic;
		frontier = new ArrayList<SearchPoint>();
		explored = new ArrayList<SearchPoint>();
		// Load the frontier with the start point
		frontier.add(startPoint);
	}

	/*
	 * Explores the highest priority (lowest expected cost) node in the frontier
	 * and adds neighboring nodes to the frontier for further exploration. Does
	 * nothing if the end point has been found.
	 */
	public void exploreNextNode() {
		// Do nothing if search is complete
		if (isComplete()) {
			return;
		}

		Collections.sort(frontier);
		
		// Move the node with lowest f() from frontier to explored
		SearchPoint bestPoint = frontier.remove(0);
		explored.add(bestPoint);

		addNeighborsToFrontier(bestPoint);
	}

	/*
	 * Returns an ArrayList of Map.Points that represents the SearchPoints in
	 * the frontier.
	 */
	public ArrayList<Point> getFrontier() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < frontier.size(); i++) {
			points.add(frontier.get(i).mapPoint);
		}
		return points;
	}

	/*
	 * Returns an ArrayList of Map.Points that represents the SearchPoints that
	 * have been explored.
	 */
	public ArrayList<Point> getExplored() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < explored.size(); i++) {
			points.add(explored.get(i).mapPoint);
		}
		return points;
	}

	/*
	 * Returns true when a search is complete: Either the end has been found, or
	 * the end cannot be reached.
	 */
	public boolean isComplete() {
		// Search explored list for the goal point
		for(Point p : getExplored()) {
			if (p.equals(goalPoint.mapPoint)) {
				return true;
			}
		}
		// Return true if no solution is found
		if (frontier.isEmpty()) {
			return true;
		}
		// Otherwise, return false
		return false;
	}

	/*
	 * Returns a list of the points along the path from start to end. The points
	 * are ordered by position in path from start to end. Returns an empty list
	 * if no solution was found.
	 */
	public List<Point> getSolution() {
		if (explored.isEmpty()) {
			return Collections.emptyList();
		}
		// Grab the end point from the end of the explored list
		SearchPoint point = explored.get(explored.size() - 1);

		// Return an empty array list if there is no solution
		if (!point.equals(goalPoint)) {
			return Collections.emptyList();
		}
		
		ArrayList<Point> solution = new ArrayList<Point>();
		// Traceback over the prev pointers
		while (point != null) {
			solution.add(point.mapPoint);
			point = point.previous;
		}
		
		Collections.reverse(solution);

		return solution;
	}
	
	/*
	 * Adds the neighbors of the point parameter to the frontier
	 * to be searched later. If a point already in the frontier 
	 * can be reached faster through this point, it is updated.
	 */
	private void addNeighborsToFrontier(SearchPoint point) {
		ArrayList<Point> neighbors = point.mapPoint.neighbors;
		for (Point neighbor : neighbors) {
			// Add completely new points to the frontier
			addNewPointToFrontier(neighbor, point);
			
			// Replace points in the frontier if a shorter path to them is found
			if (getFrontier().contains(neighbor)) {
				replaceFrontierPoint(neighbor, point);
			}
		}
	}
	
	/*
	 * Adds the point to the frontier if it is not already in
	 * the frontier or explored.
	 */
	private void addNewPointToFrontier(Point p, SearchPoint previous) {
		if (!getFrontier().contains(p) && !getExplored().contains(p)) {
			frontier.add(new SearchPoint(this.startPoint.mapPoint, this.goalPoint.mapPoint, this.heuristic,
					p, previous));
		}
	}
	
	/*
	 * Replaces a point in the frontier if a shorter path
	 * to that point is found.
	 */
	private void replaceFrontierPoint(Point p, SearchPoint previous) {
		SearchPoint newPoint = new SearchPoint(this.startPoint.mapPoint, this.goalPoint.mapPoint, this.heuristic,
				p, previous);
		for (int j = 0; j < frontier.size(); j++) {
			if (frontier.get(j).equals(newPoint)
					&& frontier.get(j).distanceFromStart() > newPoint.distanceFromStart()) {
				frontier.set(j, newPoint);
			}
		}
	}

}
