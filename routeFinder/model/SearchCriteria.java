package routeFinder.model;

public class SearchCriteria {

	public SearchPoint startPoint;
	public SearchPoint goalPoint;
	public int heuristic;

	public SearchCriteria(MapPoint start, MapPoint end, int heuristic) {
		this.startPoint = new SearchPoint(start, end, heuristic, start, null);
		this.goalPoint = new SearchPoint(start, end, heuristic, end, null);
		this.heuristic = heuristic;
	}
}
