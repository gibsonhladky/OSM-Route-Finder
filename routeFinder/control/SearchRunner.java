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
	
	private SearchState state;

	public Map map;
	
	private PApplet applet;

	private Point guiDragging;
	private Point guiStart;
	private Point guiEnd;
	private boolean guiPointsMoved;
	
	private AStarSearch search;

	
	public SearchRunner(PApplet applet) {
		this.applet = applet;
		this.state = SearchState.IDLE;
		guiPointsMoved = false;
	}
	
	public Point searchStartPoint() {
		return guiStart;
	}
	
	public Point searchEndPoint() {
		return guiEnd;
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
	
	/*
	 * Sets the start and end GUI points to an initial state. The points are set
	 * apart to allow easier usability.
	 */
	public void initializeGuiPoints() {
		guiStart = new Point(applet.width * 2 / 10, applet.height / 2);
		guiEnd = new Point(applet.width * 8 / 10, applet.height / 2);
		placePoints();
	}
	
	public void openMap(Document mapData, int width, int height) {
		MapLoader loader = new MapLoader(width, height);
		map = loader.loadMap(mapData);
	}
	
	/*
	 * Updates the position of the start and end points.
	 * Allows the user to drag the points to new positions.
	 * Draws the points after being called.
	 */
	public void updateStartAndEndPoints() {
		if (aDraggingAttemptHasStarted()) {
			selectAPointToDrag();
		}
		if (aPointIsBeingDragged()) {
			state = SearchState.MOVING_GUI;
			updateDraggedPointPosition();
		}
		if (draggingHasStopped()) {
			state = SearchState.IDLE;
			placePoints();
		}
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
	 * Returns true when the mouse is pressed and
	 * the user is not already dragging a point.
	 */
	private boolean aDraggingAttemptHasStarted() {
		return applet.mousePressed && guiDragging == null;
	}
	
	/*
	 * Selects a GUI point to drag based on current mouse position.
	 * If the mouse is not in range of any point, no point is set
	 * to be dragged.
	 */
	private void selectAPointToDrag() {
		double startToMouseDistance = sqr(applet.mouseX - guiStart.x) + sqr(applet.mouseY - guiStart.y);
		double endToMouseDistance = sqr(applet.mouseX - guiEnd.x) + sqr(applet.mouseY - guiEnd.y);
		if (startToMouseDistance <= 50 && startToMouseDistance < endToMouseDistance) {
			guiDragging = guiStart;
		}
		else if (endToMouseDistance <= 50) {
			guiDragging = guiEnd;
		}
	}
	
	/*
	 * Returns true if a point is currently being dragged
	 * by the user.
	 */
	private boolean aPointIsBeingDragged() {
		return applet.mousePressed && ( guiDragging != null );
	}
	
	/*
	 * Moves the dragged point to the current mouse position.
	 */
	private void updateDraggedPointPosition() {
		guiDragging.x = applet.mouseX;
		guiDragging.y = applet.mouseY;
	}
	
	/*
	 * Returns true if the user stops dragging a point.
	 */
	private boolean draggingHasStopped() {
		return !applet.mousePressed && ( guiDragging != null );
	}
	
	/*
	 * Updates the map's start and end points based on
	 * the gui's positions. Moves the gui's over the point
	 * they refer to.
	 */
	private void placePoints() {
		map.setStartPoint(guiStart);
		map.setEndPoint(guiEnd);
		
		guiStart.x = map.startPoint().x;
		guiStart.y = map.startPoint().y;
		guiEnd.x = map.endPoint().x;
		guiEnd.y = map.endPoint().y;
		
		guiPointsMoved = true;
		guiDragging = null;
	}
	
	/*
	 * Returns true if a key was pressed that matches the parameter.
	 */
	private boolean theKeyPressedIs(char c) {
		return applet.keyPressed && applet.key == c;
	}
	
	/*
	 * Square function. Returns x^2
	 */
	private double sqr(double x) {
		return Math.pow(x, 2);
	}
}
