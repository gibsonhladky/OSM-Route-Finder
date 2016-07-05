package routeFinder.view;

import processing.core.*;
import routeFinder.control.Main;
import routeFinder.model.Map;

/*
 * Original implementation by Gary Dahl
 */
public class MainApplet extends PApplet {
	public Map map; // map to main.search for path between start and end points of
	private MapView mapView; // View component to draw map to the applet
	private Main main;
	
	public boolean enterPressed; // press enter to watch entire main.search until
									// solution
	public boolean spaceWasDown; // press space repeatedly to step through
									// main.search

	public String mapFileName = "map.osm";

	// initialize window
	public void settings() {
		size(800, 600);
	}

	// load map, and initialize fields along with processing modes
	public void setup() {
		main = new Main(this);
		main.openMap(loadXML(mapFileName), this.width, this.height);
		main.initializeGuiPoints();
		map = main.map;
		mapView = new MapView(map, this);

		drawMap();
		drawTopPane();
		drawBottomPane();

		textAlign(CENTER);
		rectMode(CORNER);
	}

	// update
	public void draw() {
		drawMap();
		drawGuiPoints();
		drawTopPane();
		drawBottomPane();
		if (main.stillSearching()) {
			main.attemptToStepForwardInSearch();
			drawSearchProcess();
			drawInstructions();
			drawSolution();
			main.clearSearchOnNewSearch();
		}
		else // Search has completed
		{
			main.attemptToStartNewSearch();
			drawPromptToComputeANewSolution();
		}
		main.updateStartAndEndPoints(); // allow user to drag around end points
	}

	/*
	 * Clears the entire applet background with blue and draws the map on top.
	 */
	private void drawMap() {
		background(0, 0, 127); // clear display
		mapView.drawMap();
	}
	
	/*
	 * Draws the start and end GUI points used to
	 * change the start and end points.
	 */
	private void drawGuiPoints() {
		mapView.drawStartAndEnd(main.guiStart, main.guiEnd);
	}
	
	/*
	 * Clears the top of the applet with black.
	 */
	private void drawTopPane() {
		stroke(0);
		fill(0);
		rect(0, 0, width, ( height - map.usableHeight ) / 2);
	}
	
	/*
	 * Clears the bottom of the applet with black.
	 */
	private void drawBottomPane() {
		stroke(0);
		fill(0);
		rect(0, height - ( height - map.usableHeight ) / 2, width,
				( height - map.usableHeight ) / 2);
	}

	/*
	 * Displays the number of Points explored and the number of points in the
	 * frontier.
	 */
	private void drawSearchProcess() {
		drawFrontierProgress();
		drawExploredProgress();
	}
	
	/*
	 * Draws yellow points to the map indicating
	 * which points are currently in the frontier,
	 * and how many there are in the top bar.
	 */
	private void drawFrontierProgress() {
		stroke(color(127, 127, 0));
		fill(color(255, 255, 0));
		mapView.drawPoints(main.search.getFrontier());
		text("FRONTIER: " + main.search.getFrontier().size(), width / 2, 16);
	}
	
	/*
	 * Draws red points on the map indicating which
	 * points have been main.searched, and the number of
	 * points explored in the top bar
	 */
	private void drawExploredProgress() {
		stroke(color(127, 0, 0));
		fill(color(255, 0, 0));
		mapView.drawPoints(main.search.getExplored());
		text("EXPLORED: " + main.search.getExplored().size(), width / 2, 32);
	}

	/*
	 * Displays the instructions for proceeding with the current step.
	 */
	private void drawInstructions() {
		if (main.search.isComplete()) {
			drawPromptToComputeANewSolution();
		}
		else {
			drawPromptToContinueSearch();
		}
	}

	/*
	 * Draws the instructions for beginning a new main.search.
	 */
	private void drawPromptToComputeANewSolution() {
		fill(255);
		text("Press <0>, <1>, or <2> to find a path from the green to red circle.", width / 2, height - 32);
	}

	/*
	 * Draws the instructions to continue in a main.search.
	 */
	private void drawPromptToContinueSearch() {
		fill(255); // display prompt to continue exploring or reset main.search
		text("Press <Enter> to continue or <spacebard> to step through main.search.", width / 2, height - 32);
	}

	/*
	 * Draws the solution over the map if a solution has been found.
	 */
	private void drawSolution() {
		mapView.drawRoute(main.search.getSolution());
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "routeFinder.view.MainApplet" });
	}
}