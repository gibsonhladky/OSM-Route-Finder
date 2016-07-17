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
	
	public boolean searchIsComplete() {
		return search.isComplete();
	}
	
	public List<Point> searchSolution() {
		return search.getRoute();
	}
	
	/*
	 * Returns true if the search has not completed.
	 */
	public boolean stillSearching() {
		return search != null;
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
		while(! search.isComplete()) {
			search.exploreNextNode();
		}
	}
	
	/*
	 * Starts a new search when a key selecting the type of search is pressed.
	 */
	public void setSearchHeuristic(int heuristic) {
		state = SearchState.SEARCH_SELECTED;
		search = new AStarSearch(start, end, heuristic);
	}
	
	/*
	 * Updates the map's start and end points based on
	 * the gui's positions. Moves the gui's over the point
	 * they refer to.
	 */
	public void changeSearchPoints(Point start, Point end) {
		this.start = map.closestPointTo(start);
		this.end = map.closestPointTo(end);
	}
}
