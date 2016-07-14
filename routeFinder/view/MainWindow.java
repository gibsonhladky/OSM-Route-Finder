package routeFinder.view;

import processing.core.*;
import routeFinder.control.SearchRunner;
import routeFinder.control.SearchRunner.SearchState;
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
	
	public MainWindow(PApplet applet, SearchRunner searchRunner, Point guiStart, Point guiEnd) {
		this.applet = applet;
		this.searchRunner = searchRunner;
		
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
		if(searchRunner.state == SearchState.SEARCHING) {
			drawSolution();
		}
	}
	
	/*
	 * Draws the base for the window - the map, 
	 * gui, and top and bottom panes.
	 */
	private void drawBase() {
		drawMap();
		drawGuiPoints();
		drawInstructions();
	}

	/*
	 * Draws the instructions for beginning a new main.search.
	 */
	private void drawInstructions() {
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
	 * Draws the solution over the map if a solution has been found.
	 */
	private void drawSolution() {
		mapView.drawRoute(searchRunner.searchSolution());
	}
}
