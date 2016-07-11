package routeFinder.view;

import processing.core.*;
import routeFinder.control.SearchRunner;

/*
 * Original implementation by Gary Dahl
 */
public class MainWindow {
	private MapView mapView; // View component to draw map to the applet
	private SearchRunner searchRunner;
	
	private PApplet applet;

	private final float MAP_HEIGHT_RATIO = 0.8f;
	private final float MAP_WIDTH_RATIO = 1.0f;

	private float MAP_TOP;
	private float MAP_BOTTOM;
	
	public MainWindow(PApplet applet, SearchRunner searchRunner) {
		this.applet = applet;
		this.searchRunner = searchRunner;
		
		MAP_TOP = applet.height * ( 1 - MAP_HEIGHT_RATIO ) / 2;
		MAP_BOTTOM = applet.height * ( MAP_HEIGHT_RATIO + ( 1 - MAP_HEIGHT_RATIO ) / 2 );
	}
	
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}
	
	/*
	 * Draws the base for the window - the map, 
	 * gui, and top and bottom panes.
	 */
	public void drawBase() {
		drawMap();
		drawGuiPoints();
		drawTopPane();
		drawBottomPane();
	}
	
	/*
	 * Draws the search process over the base.
	 */
	public void drawSearch() {
		drawInstructions();
		drawSolution();
	}

	/*
	 * Draws the instructions for beginning a new main.search.
	 */
	public void drawPromptToComputeANewSolution() {
		applet.fill(255);
		applet.text("Press <0>, <1>, or <2> to find a path from the green to red circle.", applet.width / 2, applet.height - 32);
	}

	/*
	 * Clears the entire applet background with blue and draws the map on top.
	 */
	private void drawMap() {
		applet.background(0, 0, 127); // clear display
		mapView.drawMap();
	}

	/*
	 * Draws the start and end GUI points used to change the start and end
	 * points.
	 */
	private void drawGuiPoints() {
		mapView.drawStartAndEnd(searchRunner.searchStartPoint(), searchRunner.searchEndPoint());
	}

	/*
	 * Clears the top of the applet with black.
	 */
	private void drawTopPane() {
		applet.stroke(0);
		applet.fill(0);
		applet.rect(0, 0, applet.width, MAP_TOP);
	}

	/*
	 * Clears the bottom of the applet with black.
	 */
	private void drawBottomPane() {
		applet.stroke(0);
		applet.fill(0);
		applet.rect(0, MAP_BOTTOM, applet.width, applet.height);
	}

	/*
	 * Displays the instructions for proceeding with the current step.
	 */
	private void drawInstructions() {
		if (searchRunner.searchIsComplete()) {
			drawPromptToComputeANewSolution();
		}
		else {
			drawPromptToContinueSearch();
		}
	}

	/*
	 * Draws the instructions to continue in a main.search.
	 */
	private void drawPromptToContinueSearch() {
		applet.fill(255); // display prompt to continue exploring or reset main.search
		applet.text("Press <Enter> to continue or <spacebard> to step through main.search.", applet.width / 2, applet.height - 32);
	}

	/*
	 * Draws the solution over the map if a solution has been found.
	 */
	private void drawSolution() {
		mapView.drawRoute(searchRunner.searchSolution());
	}
}
