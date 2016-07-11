package routeFinder.control;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

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
		mainWindow.draw();
		updateSearch();
	}
	
	public void settings() {
		size(800, 600);
	}
	
	
	public void setup() {
		loadSearchRunner();
		mainWindow = new MainWindow(this, searchRunner);
		mainWindow.setMapView(new MapView(searchRunner.map, this));
		
		textAlign(CENTER);
		rectMode(CORNER);
	}
	
	private void updateSearch() {
		if(searchRunner.stillSearching()) {
			searchRunner.stepForwardInSearch();
			searchRunner.attemptToClearSearch();
		}
		else {
			searchRunner.attemptToStartNewSearch();
		}
		searchRunner.updateStartAndEndPoints();
	}

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
		searchRunner.initializeGuiPoints();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
