package routeFinder.control;

import processing.core.PApplet;
import routeFinder.view.MainApplet;

public class Main extends PApplet {
	
	private MainApplet mainApplet;
	
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
		mainApplet = new MainApplet(this, loadXML(mapFileName));
		textAlign(CENTER);
		rectMode(CORNER);
	}
}
