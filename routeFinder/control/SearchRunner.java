package routeFinder.control;

import java.util.List;

import org.w3c.dom.Document;

import processing.core.PApplet;
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

	
	public SearchRunner(PApplet applet) {
		this.state = SearchState.IDLE;
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
		while(! search.isComplete()) {
			search.exploreNextNode();
		}
	}
	
	/*
	 * Starts a new search when a key selecting the type of search is pressed.
	 */
	public void selectSearch(int heuristic) {
		state = SearchState.SEARCH_SELECTED;
		search = new AStarSearch(map, heuristic);
	}
	
	/*
	 * Updates the map's start and end points based on
	 * the gui's positions. Moves the gui's over the point
	 * they refer to.
	 */
	public void changeSearchPoints(Point start, Point end) {
		map.setStartPoint(start);
		map.setEndPoint(end);
		
		start.x = map.startPoint().x;
		start.y = map.startPoint().y;
		end.x = map.endPoint().x;
		end.y = map.endPoint().y;
	}
}
