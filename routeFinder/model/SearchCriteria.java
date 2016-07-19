package routeFinder.model;

public class SearchCriteria {

	public MapPoint start;
	public MapPoint end;
	public int heuristic;

	public SearchCriteria(MapPoint start, MapPoint end, int heuristic) {
		this.start = start;
		this.end = end;
		this.heuristic = heuristic;
	}
}
