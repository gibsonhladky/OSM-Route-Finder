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
	
	private PApplet applet;

	public boolean guiPointsMoved;
	
	private AStarSearch search;

	
	public SearchRunner(PApplet applet) {
		this.applet = applet;
		this.state = SearchState.IDLE;
		guiPointsMoved = false;
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
	 * The search continues one step if enter was ever pressed or space was
	 * pressed again.
	 */
	public void stepForwardInSearch() {
		
		if (state == SearchState.SEARCHING) {
			search.exploreNextNode();
		}
		else if (theKeyPressedIs('\n')) {
			state = SearchState.SEARCHING;
		}
	}
	
	/*
	 * When a new main.search is selected, clears the previous main.search.
	 */
	public void attemptToClearSearch() {
		if (guiPointsMoved || ( search.isComplete() && ( applet.key == '0' || applet.key == '1' || applet.key == '2' ) )) {
			state = SearchState.IDLE;
			search = null;
		}
	}
	
	/*
	 * Starts a new search when a key selecting the type of search is pressed.
	 */
	public void attemptToStartNewSearch() {
		if (applet.keyPressed && ( applet.key == '0' || applet.key == '1' || applet.key == '2' )) {
			state = SearchState.SEARCH_SELECTED;
			search = new AStarSearch(map, applet.key - '0');
			guiPointsMoved = false;
		}
	}
	
	/*
	 * Updates the map's start and end points based on
	 * the gui's positions. Moves the gui's over the point
	 * they refer to.
	 */
	public void placePoints(Point start, Point end) {
		map.setStartPoint(start);
		map.setEndPoint(end);
		
		start.x = map.startPoint().x;
		start.y = map.startPoint().y;
		end.x = map.endPoint().x;
		end.y = map.endPoint().y;
		
		guiPointsMoved = true;
	}
	
	/*
	 * Returns true if a key was pressed that matches the parameter.
	 */
	private boolean theKeyPressedIs(char c) {
		return applet.keyPressed && applet.key == c;
	}
}
