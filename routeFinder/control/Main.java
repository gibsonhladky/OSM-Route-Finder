package routeFinder.control;

import processing.core.PApplet;
import routeFinder.view.MainWindow;

public class Main extends PApplet {
	
	private MainWindow mainApplet;
	
	private final String mapFileName = "map.osm";

	public Main() {
		
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] {"routeFinder.control.Main"});
	}
	
	public void draw() {
		mainApplet.draw();
	}
	
	public void settings() {
		size(800, 600);
	}
	
	// load map, and initialize fields along with processing modes
	public void setup() {
		mainApplet = new MainWindow(this, loadXML(mapFileName));
		textAlign(CENTER);
		rectMode(CORNER);
	}
}
