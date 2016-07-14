package routeFinder.view;

import processing.core.*;
import routeFinder.control.SearchRunner;
import routeFinder.model.Point;

/*
 * Original implementation by Gary Dahl
 */
public class MainWindow {
	private MapView mapView; // View component to draw map to the applet
	private SearchRunner searchRunner;
	
	private PApplet applet;
	
	private Point guiStart;
	private Point guiEnd;

	private final float MAP_HEIGHT_RATIO = 0.8f;

	private float MAP_TOP;
	private float MAP_BOTTOM;
	
	public MainWindow(PApplet applet, SearchRunner searchRunner, Point guiStart, Point guiEnd) {
		this.applet = applet;
		this.searchRunner = searchRunner;
		
		MAP_TOP = applet.height * ( 1 - MAP_HEIGHT_RATIO ) / 2;
		MAP_BOTTOM = applet.height * ( MAP_HEIGHT_RATIO + ( 1 - MAP_HEIGHT_RATIO ) / 2 );
		
		this.guiStart = guiStart;
		this.guiEnd = guiEnd;
	}
	
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}
	
	/*
	 * Draws the application's current state based on
	 * the search runner.
	 */
	public void draw() {
		drawBase();
		if(searchRunner.stillSearching()) {
			drawSearch();
		}
		else {
			drawPromptToComputeANewSolution();
		}
	}
	
	/*
	 * Draws the base for the window - the map, 
	 * gui, and top and bottom panes.
	 */
	private void drawBase() {
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
	private void drawPromptToComputeANewSolution() {
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
		mapView.drawStartAndEnd(guiStart, guiEnd);
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
		applet.text("Press <Enter> to find a route.", applet.width / 2, applet.height - 32);
	}

	/*
	 * Draws the solution over the map if a solution has been found.
	 */
	private void drawSolution() {
		mapView.drawRoute(searchRunner.searchSolution());
	}
}
