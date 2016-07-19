package routeFinder.model;

public class SearchPoint extends MapPoint implements Comparable<SearchPoint> {
	
	private final SearchCriteria criteria;

	public MapPoint mapPoint;

	private double distanceFromStart;
	private boolean distanceFromStartInitialized;

	// traceback pointer for getSolution()
	public SearchPoint previous;
	
	public SearchPoint(MapPoint mapPoint, SearchPoint prev, SearchCriteria criteria) {
		super(mapPoint.getX(), mapPoint.getY());
		setNeighbors(mapPoint);
		this.criteria = criteria;
		this.mapPoint = mapPoint;
		this.previous = prev;
		distanceFromStart = 0;
		distanceFromStartInitialized = false;
	}
	
	private void setNeighbors(MapPoint mapPoint) {
		for(MapPoint neighbor : mapPoint.getNeighbors()) {
			this.addNeighbor(neighbor);
		}
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
		if (mapPoint.equals(criteria.start)) {
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
		switch (criteria.heuristic) {
		case 0:
			return (float) 0;
		case 1:
			return Math.abs(mapPoint.getX() - criteria.end.getX()) + Math.abs(mapPoint.getY() - criteria.end.getY());
		case 2:
			return criteria.end.distanceTo(this.mapPoint);
		default:
			throw new IllegalStateException("Invalid heuristic.");
		}
	}
}