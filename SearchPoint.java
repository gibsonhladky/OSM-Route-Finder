

public class SearchPoint implements Comparable<SearchPoint>
{
	/*
	 * 
	 */
	private final AStarSearch gibson_HLADKY_AStar;

	public Map.Point mapPoint;
	// TODO - add any extra member fields or methods that you would like here
	
	// g and gInitialized are used in g() to reduce recursion
	public float g;
	public boolean gInitialized;
	// traceback pointer for getSolution()
	public SearchPoint prev;
	
	// Returns the Euclidean 12 distance between any two points
	public float euclidean_dist(Map.Point a, Map.Point b) {
		return (float)(Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y,2)));
	}
	
	// SearchPoint constructor. 
	// Takes a Map Point and SearchPoint as input
	public SearchPoint(AStarSearch gibson_HLADKY_AStar, Map.Point x, SearchPoint prev) {
		this.gibson_HLADKY_AStar = gibson_HLADKY_AStar;
		this.mapPoint = x;
		g = 0;
		gInitialized = false;
		this.prev = prev;
	}
	
	// TODO - implement this method to return the minimum cost
	// necessary to travel from the start point to here
	public float minimumCostToReach()
	{	
		// Avoid high recursion costs:
		if(gInitialized) {
			return g;
		}
		
		gInitialized = true;
		// Recursively solve for g()
		// Start point initialization:
		if(this.mapPoint.equals(this.gibson_HLADKY_AStar.startPoint.mapPoint)) {
			this.g = 0;
			return g;
		}
		// All other points:
		else {
			this.g = this.prev.minimumCostToReach() + euclidean_dist(this.mapPoint, this.prev.mapPoint);
			return g;
		}
	}	
	
	// TODO - implement this method to return the heuristic estimate
	// of the remaining cost, based on the H parameter passed from main:
	// 0: always estimate zero, 1: manhattan distance, 2: euclidean l2 distance
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
		return heuristicCostToReach(this.gibson_HLADKY_AStar.H, this.gibson_HLADKY_AStar.goalPoint.mapPoint) + minimumCostToReach();
	}
	
	// TODO - override this compareTo method to help sort the points in 
	// your frontier from highest priority = lowest f(), and break ties
	// using whichever point has the lowest g()
	@Override
	public int compareTo(SearchPoint other)
	{
		// Compare on Priority:
		// lower f() = -1
		// higher f() = 1
		// equal f() = tie break on g()
		// equal f() and g() = 0
		
		if(other.expectedCost() == this.expectedCost()){
			if(other.minimumCostToReach() == this.minimumCostToReach()) 
				return 0;
			else if(other.minimumCostToReach() > this.minimumCostToReach()) 
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
	
	// TODO - override this equals to help you check whether your ArrayLists
	// already contain a SearchPoint referencing a given Map.Point
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