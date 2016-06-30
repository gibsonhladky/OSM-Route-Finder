package routeFinder;

import java.util.ArrayList;
import processing.core.*;

/*
 * Original implementation by Gary Dahl
 */
public class MainApplet extends PApplet {
	public AStarSearch search; // your implementation of A* search
	public Map map; // map to search for path between start and end points of
	private MapView mapView; // View component to draw map to the applet
	public boolean enterPressed; // press enter to watch entire search until
									// solution
	public boolean spaceWasDown; // press space repeatedly to step through
									// search

	public String mapFileName = "map.osm";

	// initialize window
	public void settings() {
		size(800, 600);
	}

	// load map, and initialize fields along with processing modes
	public void setup() {
		map = new Map(loadXML(mapFileName), this.width, this.height);
		mapView = new MapView(map, this);
		search = null;
		spaceWasDown = false;
		enterPressed = false;

		drawMap();

		textAlign(CENTER);
		rectMode(CORNER);
	}

	// update
	public void draw() {
		drawMap();

		if (search != null) {
			attemptToStepForwardInSearch();

			drawSearchProcess();
			drawInstructions();
			drawSolution();

			clearSearchOnNewSearch();
		}
		else // Search has completed
		{
			attemptToStartNewSearch();
			drawPromptToComputeANewSolution();
		}
		mapView.updateStartAndEndPoints(); // allow user to drag around end points
	}

	/*
	 * Clears the entire applet background and draws the map on top.
	 */
	private void drawMap() {
		background(0, 0, 127); // clear display
		mapView.drawMap();
	}

	/*
	 * The search continues one step if enter was ever pressed or space was
	 * pressed again.
	 */
	private void attemptToStepForwardInSearch() {
		if (enterPressed) {
			search.exploreNextNode();
		}
		else if (keyPressed && key == '\n') {
			enterPressed = true;
		}
		else if (keyPressed && key == ' ') {
			// otherwise explore one step per spacebar press
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
	 * Displays the number of Points explored and the number of points in the
	 * frontier.
	 */
	private void drawSearchProcess() {
		colorPoints(search.getFrontier(), true);
		colorPoints(search.getExplored(), false);
	}

	/*
	 * Displays the instructions for proceeding with the current step.
	 */
	private void drawInstructions() {
		if (search.isComplete()) {
			drawPromptToComputeANewSolution();
		}
		else {
			drawPromptToContinueSearch();
		}
	}

	/*
	 * Draws the instructions for beginning a new search.
	 */
	private void drawPromptToComputeANewSolution() {
		fill(255);
		text("Press <0>, <1>, or <2> to find a path from the green to red circle.", width / 2, height - 32);
	}

	/*
	 * Draws the instructions to continue in a search.
	 */
	private void drawPromptToContinueSearch() {
		fill(255); // display prompt to continue exploring or reset search
		text("Press <Enter> to continue or <spacebard> to step through search.", width / 2, height - 32);
	}

	/*
	 * Draws the solution over the map if a solution has been found.
	 */
	private void drawSolution() {
		mapView.drawRoute(search.getSolution());
	}

	/*
	 * When a new search is selected, clears the previous search.
	 */
	private void clearSearchOnNewSearch() {
		if (map.dirtyPoints || ( search.isComplete() && ( key == '0' || key == '1' || key == '2' ) )) {
			search = null;
			enterPressed = false;
		}
	}

	/*
	 * Starts a new search when a key selecting the type of search is pressed.
	 */
	private void attemptToStartNewSearch() {
		if (keyPressed && ( key == '0' || key == '1' || key == '2' )) {
			search = new AStarSearch(map, key - '0');
			map.dirtyPoints = false;
		}
	}

	/*
	 * Draws the points on the map and displays the number of points in the
	 * frontier or explored.
	 */
	public void colorPoints(ArrayList<Point> points, boolean isFrontier) {
		if (isFrontier) {
			stroke(color(127, 127, 0));
			fill(color(255, 255, 0)); // color frontier points yellow
			text("FRONTIER: " + points.size(), width / 2, 16);
		}
		else {
			stroke(color(127, 0, 0));
			fill(color(255, 0, 0)); // color explored points red
			text("EXPLORED: " + points.size(), width / 2, 32);
		}

		for (Point p : points) {
			ellipse(p.x, p.y, 4, 4);
		}
	}

	public void updateStartAndEndPoints() {
		mapView.updateStartAndEndPoints();;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "routeFinder.MainApplet" });
	}
}
