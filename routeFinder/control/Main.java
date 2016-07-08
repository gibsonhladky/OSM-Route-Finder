package routeFinder.control;

import processing.core.PApplet;
import routeFinder.view.MainWindow;
import routeFinder.view.MapView;

public class Main extends PApplet {
	
	private MainWindow mainWindow;
	private SearchRunner searchRunner;
	
	private final String mapFileName = "map.osm";
	private final float MAP_HEIGHT_RATIO = 0.8f;
	private final float MAP_WIDTH_RATIO = 1.0f;

	public Main() {
		
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] {"routeFinder.control.Main"});
	}
	
	public void draw() {
		mainWindow.drawBase();
		if(searchRunner.stillSearching()) {
			searchRunner.attemptToStepForwardInSearch();
			mainWindow.drawSearch();
			searchRunner.clearSearchOnNewSearch();
		}
		else {
			searchRunner.attemptToStartNewSearch();
			mainWindow.drawPromptToComputeANewSolution();
		}
		searchRunner.updateStartAndEndPoints();
	}
	
	public void settings() {
		size(800, 600);
	}
	
	
	public void setup() {
		searchRunner = loadSearchRunner();
		mainWindow = new MainWindow(this, searchRunner);
		mainWindow.setMapView(new MapView(searchRunner.map, this));
		
		textAlign(CENTER);
		rectMode(CORNER);
	}

	private SearchRunner loadSearchRunner() {
		int mapWidth = Math.round(width * MAP_WIDTH_RATIO);
		int mapHeight = Math.round(height * MAP_HEIGHT_RATIO);
		
		SearchRunner searchRunner = new SearchRunner(this);
		searchRunner.openMap(loadXML(mapFileName), mapWidth, mapHeight);
		searchRunner.initializeGuiPoints();
		return searchRunner;
	}
}
