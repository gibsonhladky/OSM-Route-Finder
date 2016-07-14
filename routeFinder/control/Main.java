package routeFinder.control;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import processing.core.PApplet;
import routeFinder.control.SearchRunner.SearchState;
import routeFinder.model.Point;
import routeFinder.view.MainWindow;
import routeFinder.view.MapView;

public class Main extends PApplet {
	
	private MainWindow mainWindow;
	private SearchRunner searchRunner;
	
	private final String mapFileName = "map.osm";
	private final float MAP_HEIGHT_RATIO = 0.8f;
	private final float MAP_WIDTH_RATIO = 1.0f;
	
	private Point guiDragging;
	private Point guiStart;
	private Point guiEnd;

	public Main() {
		
	}
	
	/*
	 * Called at application start to set up all 
	 * necessary pieces of the application.
	 */
	public void setup() {
		loadSearchRunner();
		mainWindow = new MainWindow(this, searchRunner, guiStart, guiEnd);
		mainWindow.setMapView(new MapView(searchRunner.map, this));
		
		textAlign(CENTER);
		rectMode(CORNER);
	}
	
	/*
	 * Driving function for a Processing PApplet.
	 * This is called by the application's runner
	 * in an infinite loop.
	 */
	public void draw() {
		mainWindow.draw();
	}
	
	public void settings() {
		size(800, 600);
	}
	
	@Override
	public void keyPressed() {
		switch(key) {
		case '0':
		case '1':
		case '2':
			searchRunner.setSearchHeuristic(key - '0');
			searchRunner.search();
		}
	}
	
	@Override
	public void mousePressed() {
		selectPointToDrag();
	}
	
	@Override
	public void mouseReleased() {
		placePoints();
		transitionTo(SearchState.IDLE);
		guiDragging = null;
	}
	
	@Override
	public void mouseDragged() {
		transitionTo(SearchState.MOVING_GUI);
		if(aPointIsBeingDragged()) {
			updateDraggedPointPosition();
		}
	}

	private void selectPointToDrag() {
		double startToMouseDistance = sqr(mouseX - guiStart.x) + sqr(mouseY - guiStart.y);
		double endToMouseDistance = sqr(mouseX - guiEnd.x) + sqr(mouseY - guiEnd.y);
		if (startToMouseDistance <= 50 && startToMouseDistance < endToMouseDistance) {
			guiDragging = guiStart;
		}
		else if (endToMouseDistance <= 50) {
			guiDragging = guiEnd;
		}
	}
	
	private void placePoints() {
		searchRunner.changeSearchPoints(guiStart, guiEnd);
		guiDragging = null;
	}
	
	private void transitionTo(SearchState newState) {
		searchRunner.state = newState;
	}
	
	private boolean aPointIsBeingDragged() {
		return guiDragging != null;
	}
	
	private void updateDraggedPointPosition() {
		guiDragging.x = mouseX;
		guiDragging.y = mouseY;
	}

	/*
	 * Loads the search runner with the map referred to in Main.
	 * Opens the map and places initial start and stop points.
	 */
	private void loadSearchRunner() {
		int mapWidth = Math.round(width * MAP_WIDTH_RATIO);
		int mapHeight = Math.round(height * MAP_HEIGHT_RATIO);
		
		try {
			File mapFile = new File(mapFileName);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.parse(mapFile);
	
			searchRunner = new SearchRunner(this);
			searchRunner.openMap(doc, mapWidth, mapHeight);
			initializeGuiPoints();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Places the GUI points at a starting position on the map.
	 */
	private void initializeGuiPoints() {
		guiStart = new Point(width * 2 / 10, height / 2);
		guiEnd = new Point(width * 8 / 10, height / 2);
		placePoints();
	}
	
	
	private double sqr(double x) {
		return Math.pow(x, 2);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] {"routeFinder.control.Main"});
	}

}
