package routeFinder.model;

public class SearchPoint implements Comparable<SearchPoint> {
	private final Point startPoint;
	private final Point goalPoint;
	private final int heuristic;

	public Point mapPoint;

	private float distanceFromStart;
	private boolean distanceFromStartInitialized;

	// traceback pointer for getSolution()
	public SearchPoint previous;

	// Takes a Map Point and SearchPoint as input
	public SearchPoint(Point startPoint, Point goalPoint, int heuristic, Point mapPoint, SearchPoint prev) {
		this.startPoint = startPoint;
		this.goalPoint = goalPoint;
		this.mapPoint = mapPoint;
		this.heuristic = heuristic;
		this.previous = prev;
		distanceFromStart = 0;
		distanceFromStartInitialized = false;
	}

	/*
	 * Returns the minimum cost to reach this point from the start point.
	 */
	public float distanceFromStart() {
		// Avoid high recursion costs:
		if (distanceFromStartInitialized) {
			return distanceFromStart;
		}

		distanceFromStartInitialized = true;
		// Recursively find the distance from start.
		// Base case
		if (this.mapPoint.equals(startPoint)) {
			distanceFromStart = 0;
			return distanceFromStart;
		}
		// Recursive case
		else {
			distanceFromStart = previous.distanceFromStart() + distanceBetween(mapPoint, previous.mapPoint);
			return distanceFromStart;
		}
	}

	/*
	 * Returns the expected cost to reach the end point from this search point.
	 */
	public float expectedCost() {
		return heuristicCostToReachEnd() + distanceFromStart();
	}

	/*
	 * Compares on priority: 
	 * <: lower expected cost OR equal expected cost and lower heuristic cost. 
	 * ==: equal expected cost and heuristic cost. 
	 * >: higher expected cost OR equal expected cost and higher heuristic cost.
	 */
	@Override
	public int compareTo(SearchPoint other) {
		if (other.expectedCost() == this.expectedCost()) {
			if (this.distanceFromStart() == other.distanceFromStart())
				return 0;
			else if (this.distanceFromStart() < other.distanceFromStart()) {
				return -1;
			}
			else {
				return 1;
			}
		}
		else if (this.expectedCost() > other.expectedCost()) {
			return 1;
		}
		else {
			return -1;
		}
	}

	/*
	 * Returns true if and only if the search points refer to the same map
	 * points.
	 */
	@Override
	public boolean equals(Object other) {
		if (other.getClass() != this.getClass()) {
			return false;
		}

		return ( (SearchPoint) other ).mapPoint.equals(this.mapPoint);
	}

	// Returns the Euclidean 12 distance between any two points
	public float distanceBetween(Point a, Point b) {
		return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}

	/*
	 * Returns the estimated cost to reach the goal point based on the specified
	 * heuristic: 0: always estimate zero, 1: manhattan distance, 2: euclidean
	 * l2 distance
	 */
	private float heuristicCostToReachEnd() {
		if (heuristic == 0) {
			return (float) 0;
		}
		else if (heuristic == 1) {
			return Math.abs(mapPoint.x - goalPoint.x) + Math.abs(mapPoint.y - goalPoint.y);
		}
		else {
			return distanceBetween(goalPoint, this.mapPoint);
		}
	}
}