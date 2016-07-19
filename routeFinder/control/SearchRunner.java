package routeFinder.control;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import routeFinder.model.Map;
import routeFinder.model.MapLoader;
import routeFinder.model.MapPoint;
import routeFinder.model.Point;
import search.AStarSearch;

public class SearchRunner {
	
	public enum SearchState {
		IDLE, MOVING_GUI, SEARCH_SELECTED, SEARCHING
	}
	
	public SearchState state;

	public Map map;
	
	private AStarSearch search;
	private MapPoint start;
	private MapPoint end;
	
	public SearchRunner() {
		this.state = SearchState.IDLE;

		start = new MapPoint(0, 0);
		end = new MapPoint(0, 0);
	}
	
	public List<Point> searchSolution() {
		List<Point> result = new ArrayList<Point>(search.getRoute().size());
		for(MapPoint each : search.getRoute()) {
			result.add(each);
		}
		return result;
	}
	
	public void openMap(Document mapData, int width, int height) {
		MapLoader loader = new MapLoader(width, height);
		map = loader.loadMap(mapData);
	}
	
	/*
	 * Runs the current search to completion.
	 */
	public void search() {
		state = SearchState.SEARCHING;
		search.search();
	}
	
	/*
	 * Starts a new search when a key selecting the type of search is pressed.
	 */
	public void setSearchHeuristic(int heuristic) {
		state = SearchState.SEARCH_SELECTED;
		search = new AStarSearch(start, end, heuristic);
	}
	
	/*
	 * Modifies the start and end locations of the search.
	 */
	public void setSearchPoints(Point start, Point end) {
		this.start = map.closestPointTo(start);
		this.end = map.closestPointTo(end);
	}
}
