package routeFinder.control;

import processing.core.PApplet;
import processing.data.XML;
import routeFinder.model.AStarSearch;
import routeFinder.model.Map;
import routeFinder.model.Point;

public class Main {

	public Map map;
	
	private PApplet applet;

	private Point guiDragging;
	public Point guiStart;
	public Point guiEnd;
	
	public boolean enterPressed; // press enter to watch entire search until
	// solution
	public boolean spaceWasDown; // press space repeatedly to step through
	// search
	public AStarSearch search;

	
	public Main(PApplet applet) {
		this.applet = applet;
	}
	
	/*
	 * Sets the start and end GUI points to an initial state. The points are set
	 * apart to allow easier usability.
	 */
	public void initializeGuiPoints() {
		guiStart = new Point(applet.width * 2 / 10, applet.height / 2);
		guiEnd = new Point(applet.width * 8 / 10, applet.height / 2);
		guiDragging = null;
		moveEndPointsToClosestStreet();
	}
	
	private void moveEndPointsToClosestStreet() {
		map.moveEndPointsToClosestStreet(guiStart, guiEnd);
	}
	
	public void openMap(XML mapData, int width, int height) {
		map = new Map(mapData, width, height);
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
			updateDraggedPointPosition();
		}
		if (draggingHasStopped()) {
			placePoint();
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
	 * Places the currently dragged point on the map.
	 */
	private void placePoint() {
		map.moveEndPointsToClosestStreet(guiStart, guiEnd);
		guiDragging = null;
	}
	
	/*
	 * Returns true if the search has not completed.
	 */
	public boolean stillSearching() {
		return search != null;
	}
	
	/*
	 * The search continues one step if enter was ever pressed or space was
	 * pressed again.
	 */
	public void attemptToStepForwardInSearch() {
		if (enterPressed) {
			search.exploreNextNode();
		}
		else if (theKeyPressedIs('\n')) {
			enterPressed = true;
		}
		else if (theKeyPressedIs(' ')) {
			// explore one step per spacebar press
			if (!spaceWasDown) {
				search.exploreNextNode();
			}
			spaceWasDown = true;
		}
		else {
			spaceWasDown = false;
		}
	}
	
	/*
	 * Returns true if a key was pressed that matches the parameter.
	 */
	private boolean theKeyPressedIs(char c) {
		return applet.keyPressed && applet.key == c;
	}
	
	/*
	 * When a new main.search is selected, clears the previous main.search.
	 */
	public void clearSearchOnNewSearch() {
		if (map.dirtyPoints || ( search.isComplete() && ( applet.key == '0' || applet.key == '1' || applet.key == '2' ) )) {
			search = null;
			enterPressed = false;
		}
	}
	
	/*
	 * Starts a new search when a key selecting the type of search is pressed.
	 */
	public void attemptToStartNewSearch() {
		if (applet.keyPressed && ( applet.key == '0' || applet.key == '1' || applet.key == '2' )) {
			search = new AStarSearch(map, applet.key - '0');
			map.dirtyPoints = false;
		}
	}
	
	/*
	 * Square function. Returns x^2
	 */
	private double sqr(double x) {
		return Math.pow(x, 2);
	}
}
