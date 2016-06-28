import java.util.ArrayList;

/*
 * Original structure provided by Gary Dahl
 */
public class AStarSearch 
{		
	public ArrayList<SearchPoint> frontier;
	public ArrayList<SearchPoint> explored;

	// Two SearchPoints to keep track of the start and goal
	public SearchPoint startPoint, goalPoint;
	
	// The chosen heuristic
	public int H;
	
	// TODO - implement this constructor to initialize your member variables
	// and search, by adding the start point to your frontier.  The parameter
	// H indicates which heuristic you should use while searching:
	// 0: always estimate zero, 1: manhattan distance, 2: euclidean l2 distance
	public AStarSearch(Map map, int H)
	{
		this.startPoint = new SearchPoint(map.start, map.end, H, map.start, null);
		this.goalPoint = new SearchPoint(map.start, map.end, H, map.end, null);
		this.H = H;
		frontier = new ArrayList<SearchPoint>();
		explored = new ArrayList<SearchPoint>();
		// Load the frontier with the start point
		frontier.add(startPoint);
	}
	
	/*
	 * Explores the highest priority (lowest expected cost) node in the frontier
	 * and adds neighboring nodes to the frontier for further exploration.
	 * Does nothing if the end point has been found.
	 */
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
				frontier.add(new SearchPoint(this.startPoint.mapPoint, this.goalPoint.mapPoint, this.H, neighbors.get(i), currPoint));
			}
			// Replace points in the frontier if a shorter path to them is found
			if(getFrontier().contains(neighbors.get(i))) {
				SearchPoint newPoint = new SearchPoint(this.startPoint.mapPoint, this.goalPoint.mapPoint, this.H, neighbors.get(i), currPoint);
				for(int j = 0; j < frontier.size(); j++) {
					if(frontier.get(j).equals(newPoint) && frontier.get(j).distanceFromStart() > newPoint.distanceFromStart()) {
						frontier.set(j, newPoint);
					}
				}
			}
		}
	}

	/*
	 *  Returns an ArrayList of Map.Points that represents the SearchPoints in the frontier.
	 */
	public ArrayList<Map.Point> getFrontier()
	{
		ArrayList<Map.Point> points = new ArrayList<Map.Point>(); 
		for(int i = 0; i < frontier.size(); i++) {
			points.add(frontier.get(i).mapPoint);
		}
		return points;
	}
	
	/*
	 *  Returns an ArrayList of Map.Points that represents the SearchPoints that have been explored.
	 */
	public ArrayList<Map.Point> getExplored()
	{
		ArrayList<Map.Point> points = new ArrayList<Map.Point>(); 
		for(int i = 0; i < explored.size(); i++) {
			points.add(explored.get(i).mapPoint);
		}
		return points;
	}

	/*
	 *  Returns true when a search is complete:
	 *  Either the end has been found, or the end cannot be reached.
	 */
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

	/*
	 * Returns a list of the points along the path from start to end.
	 * The points are ordered by position in path from start to end.
	 * Returns an empty list if no solution was found.
	 */
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
	
}
