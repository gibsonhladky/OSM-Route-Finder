

public class SearchPoint implements Comparable<SearchPoint>
{
	private final Map.Point startPoint;
	private final Map.Point goalPoint;
	private final int heuristic;

	public Map.Point mapPoint;
	
	// g and gInitialized are used in g() to reduce recursion
	public float distanceFromStart;
	public boolean distanceFromStartInitialized;
	// traceback pointer for getSolution()
	public SearchPoint prev;
	
	// Returns the Euclidean 12 distance between any two points
	public float euclidean_dist(Map.Point a, Map.Point b) {
		return (float)(Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y,2)));
	}
	
	// Takes a Map Point and SearchPoint as input
	public SearchPoint(Map.Point startPoint, Map.Point goalPoint, int heuristic, Map.Point mapPoint, SearchPoint prev) {
		this.startPoint = startPoint;
		this.goalPoint = goalPoint;
		this.mapPoint = mapPoint;
		this.heuristic = heuristic;
		distanceFromStart = 0;
		distanceFromStartInitialized = false;
		this.prev = prev;
	}
	
	/*
	 * Returns the minimum cost to reach this point from the start point.
	 */
	public float distanceFromStart()
	{	
		// Avoid high recursion costs:
		if(distanceFromStartInitialized) {
			return distanceFromStart;
		}
		
		distanceFromStartInitialized = true;
		// Recursively solve for g()
		// Start point initialization:
		if(this.mapPoint.equals(startPoint)) {
			this.distanceFromStart = 0;
			return distanceFromStart;
		}
		// All other points:
		else {
			this.distanceFromStart = this.prev.distanceFromStart() + euclidean_dist(this.mapPoint, this.prev.mapPoint);
			return distanceFromStart;
		}
	}	
	
	/*
	 * Returns the estimated cost to reach the goal point based on the
	 * specified heuristic:
	 * 0: always estimate zero, 1: manhattan distance, 2: euclidean l2 distance
	 */
	public float heuristicCostToReach(int heuristic, Map.Point goal)
	{
		// Zero Heuristic
		if(heuristic == 0)
			return (float)0;
		// Manhattan Distance
		else if(heuristic == 1) {
			return Math.abs(mapPoint.x - goal.x) + Math.abs(mapPoint.y - goal.y);
		}
		// Euclidean 12 distance
		else {
			return euclidean_dist(goal, this.mapPoint);
		}
	}
	
	/*
	 * Returns the expected cost to reach the end point from this search point.
	 */
	public float expectedCost()
	{
		return heuristicCostToReach(heuristic, goalPoint) + distanceFromStart();
	}
	
	/*
	 * Compares on priority:
	 * <: lower expected cost OR equal expected cost and lower heuristic cost.
	 * ==: equal expected cost and heuristic cost.
	 * >: higher expected cost OR equal expected cost and higher heuristic cost.
	 */
	@Override
	public int compareTo(SearchPoint other)
	{
		if(other.expectedCost() == this.expectedCost()){
			if(other.distanceFromStart() == this.distanceFromStart()) 
				return 0;
			else if(other.distanceFromStart() > this.distanceFromStart()) 
				return -1;
			else 
				return 1;
		}
		else if(other.expectedCost() < this.expectedCost()) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	/*
	 * Returns true if and only if the search points refer to
	 * the same map points.
	 */
	@Override
	public boolean equals(Object other)
	{
		// Return false for non-SearchPoint objects
		if(other.getClass() != this.getClass()){
			return false;
		}
		
		// Convert 'other' to a SearchPoint object
		SearchPoint newsp = (SearchPoint) other;
		
		// Return true if both SearchPoints have the same Map.Point
		if(newsp.mapPoint.equals(this.mapPoint)){
			return true;
		}
		return false;
	}
}