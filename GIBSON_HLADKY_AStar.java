import java.util.ArrayList;
import java.util.Collections;

/*
 * Original structure provided by Gary Dahl
 */
public class GIBSON_HLADKY_AStar 
{		
	public ArrayList<SearchPoint> frontier;
	public ArrayList<SearchPoint> explored;
	// TODO - add any extra member fields that you would like here 

	// Two SearchPoints to keep track of our start and goal
	public SearchPoint startPoint, goalPoint;
	
	// The chosen heuristic
	public int H;
	
	public class SearchPoint implements Comparable<SearchPoint>
	{
		public Map.Point mapPoint;
		// TODO - add any extra member fields or methods that you would like here
		
		// g and gInitialized are used in g() to reduce recursion
		public float g;
		public boolean gInitialized;
		// traceback pointer for getSolution()
		public SearchPoint prev;
		
		// Returns the Euclidean 12 distance between any two points
		public float euclidean_dist(SearchPoint a, SearchPoint b) {
			return (float)(Math.sqrt(Math.pow(a.mapPoint.x - b.mapPoint.x, 2) + Math.pow(a.mapPoint.y - b.mapPoint.y,2)));
		}
		
		// SearchPoint constructor. 
		// Takes a Map Point and SearchPoint as input
		public SearchPoint(Map.Point x, SearchPoint prev) {
			this.mapPoint = x;
			g = 0;
			gInitialized = false;
			this.prev = prev;
		}
		
		// TODO - implement this method to return the minimum cost
		// necessary to travel from the start point to here
		public float g()
		{	
			// Avoid high recursion costs:
			if(gInitialized) {
				return g;
			}
			
			gInitialized = true;
			// Recursively solve for g()
			// Start point initialization:
			if(this.mapPoint.equals(startPoint.mapPoint)) {
				this.g = 0;
				return g;
			}
			// All other points:
			else {
				this.g = this.prev.g() + euclidean_dist(this, this.prev);
				return g;
			}
		}	
		
		// TODO - implement this method to return the heuristic estimate
		// of the remaining cost, based on the H parameter passed from main:
		// 0: always estimate zero, 1: manhattan distance, 2: euclidean l2 distance
		public float h()
		{
			// Zero Heuristic
			if(H == 0)
				return (float)0;
			// Manhattan Distance
			else if(H == 1) {
				return Math.abs(mapPoint.x - goalPoint.mapPoint.x) + Math.abs(mapPoint.y - goalPoint.mapPoint.y);
			}
			// Euclidean 12 distance
			else {
				return euclidean_dist(goalPoint, this);
			}
		}
		
		// TODO - implement this method to return to final priority for this
		// point, which include the cost spent to reach it and the heuristic 
		// estimate of the remaining cost
		public float f()
		{
			return h() + g();
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
			
			if(other.f() == this.f()){
				if(other.g() == this.g()) 
					return 0;
				else if(other.g() > this.g()) 
					return -1;
				else 
					return 1;
			}
			else if(other.f() < this.f()) {
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
	
	// TODO - implement this constructor to initialize your member variables
	// and search, by adding the start point to your frontier.  The parameter
	// H indicates which heuristic you should use while searching:
	// 0: always estimate zero, 1: manhattan distance, 2: euclidean l2 distance
	public GIBSON_HLADKY_AStar(Map map, int H)
	{
		this.startPoint = new SearchPoint(map.start, null);
		this.goalPoint = new SearchPoint(map.end, null);
		this.H = H;
		frontier = new ArrayList<SearchPoint>();
		explored = new ArrayList<SearchPoint>();
		// Load the frontier with the start point
		frontier.add(startPoint);
	}
	
	// TODO - implement this method to explore the single highest priority
	// and lowest f() SearchPoint from your frontier.  This method will be 
	// called multiple times from Main to help you visualize the search.
	// This method should not do anything, if your search is complete.
	public void exploreNextNode() 
	{
		// Do nothing if search is complete
		if(isComplete()) return;
		
		// Simple bubble sort of the frontier. Could be moved to helper method.
		SearchPoint temp;
		for(int i = 0; i < frontier.size(); i++) {
			for(int j = frontier.size()-1; j > i; j--) {
				if(frontier.get(j).compareTo(frontier.get(j-1)) == -1) {
					temp = frontier.get(j);
					frontier.set(j, frontier.get(j-1));
					frontier.set(j-1, temp);
				}
			}
		}
		// Move the node with lowest f() from frontier to explored
		SearchPoint currPoint = frontier.remove(0);
		explored.add(currPoint);
		
		// Add the new neighboring points to the frontier
		ArrayList<Map.Point> neighbors = currPoint.mapPoint.neighbors;
		for(int i = 0; i < neighbors.size(); i++) {
			// Add completely new points to the frontier
			if(!getFrontier().contains(neighbors.get(i)) && !getExplored().contains(neighbors.get(i))) {
				frontier.add(new SearchPoint(neighbors.get(i), currPoint));
			}
			// Replace points in the frontier if a shorter path to them is found
			if(getFrontier().contains(neighbors.get(i))) {
				SearchPoint newPoint = new SearchPoint(neighbors.get(i), currPoint);
				for(int j = 0; j < frontier.size(); j++) {
					if(frontier.get(j).equals(newPoint) && frontier.get(j).g() > newPoint.g()) {
						frontier.set(j, newPoint);
					}
				}
			}
		}
	}

	// TODO - implement this method to return an ArrayList of Map.Points
	// that represents the SearchPoints in your frontier.
	public ArrayList<Map.Point> getFrontier()
	{
		ArrayList<Map.Point> points = new ArrayList<Map.Point>(); 
		for(int i = 0; i < frontier.size(); i++) {
			points.add(frontier.get(i).mapPoint);
		}
		return points;
	}
	
	// TODO - implement this method to return an ArrayList of Map.Points
	// that represents the SearchPoints that you have explored.
	public ArrayList<Map.Point> getExplored()
	{
		ArrayList<Map.Point> points = new ArrayList<Map.Point>(); 
		for(int i = 0; i < explored.size(); i++) {
			points.add(explored.get(i).mapPoint);
		}
		return points;
	}

	// TODO - implement this method to return true only after a solution
	// has been found, or you have determined that no solution is possible.
	public boolean isComplete()
	{
		// Search explored list for the goal point
		ArrayList<Map.Point> exploredPoints = getExplored();
		for(int i = 0; i < exploredPoints.size(); i++) {
			if(exploredPoints.get(i).equals(goalPoint.mapPoint)) {
				return true;
			}
		}
		// Return true if no solution is found
		if(frontier.isEmpty()) return true;
		// Otherwise, return false
		return false;
	}

	// TODO - implement this method to return an ArrayList of the Map.Points
	// that are along the path that you have found from the start to end.  
	// These points must be in the ArrayList in the order that they are 
	// traversed while moving along the path that you have found.
	public ArrayList<Map.Point> getSolution()
	{
		// Grab the end point from the end of the explored list
		SearchPoint point = explored.get(explored.size()-1);
		
		// Return an empty array list if there is no solution
		if(!point.equals(goalPoint)) {
			return new ArrayList<Map.Point>();
		}
		ArrayList<Map.Point> solution = new ArrayList<Map.Point>();
		// Traceback over the prev pointers
		while(point != null) {
			solution.add(0, point.mapPoint);
			point = point.prev;
		}
		
		return solution;
	}	
	
	// Use a simple bubble sort to sort the frontier
	// This could be improved using a faster search algorithm
	public void sortFrontier() {
		
	}
}
