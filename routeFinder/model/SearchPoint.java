package routeFinder.model;

public class SearchPoint extends MapPoint implements Comparable<SearchPoint> {
	
	private final MapPoint startPoint;
	private final MapPoint goalPoint;
	private final int heuristic;

	public MapPoint mapPoint;

	private double distanceFromStart;
	private boolean distanceFromStartInitialized;

	// traceback pointer for getSolution()
	public SearchPoint previous;
	
	public SearchPoint(MapPoint mapPoint, SearchPoint prev, SearchCriteria criteria) {
		super(mapPoint.getX(), mapPoint.getY());
		
		this.startPoint = criteria.start;
		this.goalPoint = criteria.end;
		this.heuristic = criteria.heuristic;
		this.mapPoint = mapPoint;
		this.previous = prev;
		distanceFromStart = 0;
		distanceFromStartInitialized = false;
	}

	@Override
	public int compareTo(SearchPoint other) {
		if (this.expectedCostToReachEnd() == other.expectedCostToReachEnd()) {
			if (this.distanceFromStart() == other.distanceFromStart())
				return 0;
			else if (this.distanceFromStart() < other.distanceFromStart()) {
				return -1;
			}
			else {
				return 1;
			}
		}
		else if (this.expectedCostToReachEnd() > other.expectedCostToReachEnd()) {
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

	private double distanceFromStart() {
		// Avoid high recursion costs:
		if (!distanceFromStartInitialized) {
			distanceFromStartInitialized = true;
			calculateDistanceFromStart();
		}
		return distanceFromStart;
	}

	/*
	 * Returns the expected cost to reach the end point from this search point.
	 */
	private double expectedCostToReachEnd() {
		return heuristicCostToReachEnd() + distanceFromStart();
	}
	
	private void calculateDistanceFromStart() {
		if (mapPoint.equals(startPoint)) {
			distanceFromStart = 0;
		}
		else {
			distanceFromStart = previous.distanceFromStart() + mapPoint.distanceTo(previous.mapPoint);
		}
	}

	/*
	 * Returns the estimated cost to reach the goal point based on the specified
	 * heuristic: 0: always estimate zero, 1: manhattan distance, 2: euclidean
	 * l2 distance
	 */
	private double heuristicCostToReachEnd() {
		switch (heuristic) {
		case 0:
			return (float) 0;
		case 1:
			return Math.abs(mapPoint.getX() - goalPoint.getX()) + Math.abs(mapPoint.getY() - goalPoint.getY());
		case 2:
			return goalPoint.distanceTo(this.mapPoint);
		default:
			throw new IllegalStateException("Invalid heuristic.");
		}
	}
}