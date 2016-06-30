package routeFinder;

import java.util.ArrayList;

import processing.core.PApplet;

public class MapView {

	private PApplet applet;
	private Map map;

	public MapView(Map map, PApplet p) {
		this.map = map;
		applet = p;
	}

	/*
	 * Draws the map on the applet.
	 */
	public void drawMap() {
		applet.stroke(127);
		applet.strokeWeight(1);
		for (Street street : map.allStreets) {
			for (int i = 1; i < street.points.size(); i++) {
				applet.line(street.points.get(i - 1).x, street.points.get(i - 1).y, street.points.get(i).x,
						street.points.get(i).y);
			}
		}

		applet.stroke(0);
		applet.fill(0);
		applet.rect(0, 0, applet.width, ( applet.height - map.usableHeight ) / 2);
		applet.rect(0, applet.height - ( applet.height - map.usableHeight ) / 2, applet.width,
				( applet.height - map.usableHeight ) / 2);
	}
	
	public void drawSolution(ArrayList<Point> solutionPoints) {
		applet.stroke(255); // draw white lines between points
		applet.strokeWeight(2);
		for (int i = 1; i < solutionPoints.size(); i++) {
			applet.line(solutionPoints.get(i - 1).x, solutionPoints.get(i - 1).y, solutionPoints.get(i).x, solutionPoints.get(i).y);
		}
	}

}
