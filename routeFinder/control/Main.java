package routeFinder.control;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import processing.core.PApplet;
import routeFinder.control.SearchRunner.SearchState;
import routeFinder.model.MapPoint;
import routeFinder.view.MainWindow;
import routeFinder.view.MapView;

public class Main extends PApplet {
	
	private static final int MOUSE_BUFFER = 50;
	private MainWindow mainWindow;
	private SearchRunner searchRunner;
	
	private final String mapFileName = "map.osm";
	private final float MAP_HEIGHT_RATIO = 0.8f;
	private final float MAP_WIDTH_RATIO = 1.0f;
	
	private MapPoint guiDragging;
	private MapPoint guiStart;
	private MapPoint guiEnd;

	public Main() {
		
	}
	
	public void settings() {
		size(800, 600);
	}
	
	/*
	 * Called at application start to set up all 
	 * necessary pieces of the application.
	 */
	public void setup() {
		loadSearchRunner();
		openMap();
		initializeGuiPoints();
		
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
		if(aPointIsBeingDragged()) {
			transitionTo(SearchState.MOVING_GUI);
		}
	}
	
	@Override
	public void mouseReleased() {
		placePoints();
		if(searchRunner.state == SearchState.MOVING_GUI) {
			transitionTo(SearchState.IDLE);
		}
		guiDragging = null;
	}
	
	@Override
	public void mouseDragged() {
		if(aPointIsBeingDragged()) {
			updateDraggedPointPosition();
		}
	}

	private void selectPointToDrag() {
		if (aPointIsUnderMouse()) {
			guiDragging = distanceToMouse(guiStart) < distanceToMouse(guiEnd) ? guiStart : guiEnd;
		}
	}
	
	private boolean aPointIsUnderMouse() {
		return distanceToMouse(guiStart) <= MOUSE_BUFFER ||
				distanceToMouse(guiEnd) <= MOUSE_BUFFER;
	}
	
	private double distanceToMouse(MapPoint point) {
		return sqr(mouseX - point.x) + sqr(mouseY - point.y);
	}
	
	private void placePoints() {
		searchRunner.setSearchPoints(guiStart, guiEnd);
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
		searchRunner = new SearchRunner();
	}
	
	private void openMap() {
		try {
			Document mapDoc = parseMapFile();
			int mapWidth = Math.round(width * MAP_WIDTH_RATIO);
			int mapHeight = Math.round(height * MAP_HEIGHT_RATIO);
			searchRunner.openMap(mapDoc, mapWidth, mapHeight);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Document parseMapFile() throws Exception {
		File mapFile = new File(mapFileName);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    Document doc = docBuilder.parse(mapFile);
	    return doc;
	}
	
	/*
	 * Places the GUI points at a starting position on the map.
	 */
	private void initializeGuiPoints() {
		guiStart = new MapPoint(width * 2 / 10, height / 2);
		guiEnd = new MapPoint(width * 8 / 10, height / 2);
		placePoints();
	}
	
	
	private double sqr(double x) {
		return Math.pow(x, 2);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] {"routeFinder.control.Main"});
	}

}
