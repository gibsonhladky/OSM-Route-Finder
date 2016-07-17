package routeFinder.control;

import java.util.List;

import org.w3c.dom.Document;

import routeFinder.model.AStarSearch;
import routeFinder.model.Map;
import routeFinder.model.MapLoader;
import routeFinder.model.Point;

public class SearchRunner {
	
	public enum SearchState {
		IDLE, MOVING_GUI, SEARCH_SELECTED, SEARCHING
	}
	
	public SearchState state;

	public Map map;
	
	private AStarSearch search;
	private Point start;
	private Point end;
	
	public SearchRunner() {
		this.state = SearchState.IDLE;

		start = new Point(0, 0);
		end = new Point(0, 0);
	}
	
	public List<Point> searchSolution() {
		return search.getRoute();
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
