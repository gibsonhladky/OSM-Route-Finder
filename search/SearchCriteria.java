package search;

import routeFinder.model.MapPoint;

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
