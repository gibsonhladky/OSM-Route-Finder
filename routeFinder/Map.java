package routeFinder;
import java.util.*;

import processing.core.*;
import processing.data.*;

/*
 * Original implementation by Gary Dahl
 */
public class Map 
{
	// access to all points and streets
	public ArrayList<Point> allPoints;
	public ArrayList<Street> allStreets;
	// actual points to search between
	public Point start;
	public Point end;
	
	// A reference to the Processing PApplet used to draw
	// the map to screen
	private PApplet applet;
	// extra points to manipulate with gui
	public Point guiStart;
	public Point guiEnd;
	// reference to which guiPoint is being dragged, or null
	public Point guiDragging;
	// actual points to search between have changed
	public boolean dirtyPoints;
	// based on aspect ration of map data vs 800x600 window
	public float usableHeight;

	public Map(String mapFileName, PApplet p)
	{
		this.applet = p;
		initializePointsAndStreets();
		loadMap(mapFileName);
		initializeGuiPoints();
	}
	
	public void loadMap(String mapFileName)
	{
		XML xml = openXML(mapFileName);
		
		
		// TODO: Extract a MapBounds class
		// read dimensions to create proportional window
		//             and to scale myPoint positions
	    float minlat = xml.getChild("bounds").getFloat("minlat");
	    float minlon = xml.getChild("bounds").getFloat("minlon");
	    float maxlat = xml.getChild("bounds").getFloat("maxlat");
	    float maxlon = xml.getChild("bounds").getFloat("maxlon");
	    float latRange = maxlat - minlat;
	    float lonRange = maxlon - minlon;
	    usableHeight = (800 * latRange / lonRange);

	    // read points
	    XML nodes[] = xml.getChildren("node");
		Hashtable<Long,Integer> indexConvert = new Hashtable<Long,Integer>();
	    for(XML node : nodes)
	    {
	    	if(!node.hasAttribute("id") || !node.hasAttribute("lat") || !node.hasAttribute("lon")) continue;
	    	
	    	long id = node.getLong("id", -1);
	    	float lat = node.getFloat("lat");
	    	float lon = node.getFloat("lon");
	    	Point point = new Point(applet.width * (lon-minlon)/lonRange, applet.height - (usableHeight * (lat-minlat)/latRange) - (applet.height-usableHeight)/2);
	    	allPoints.add(point);
	    	indexConvert.put(id, allPoints.indexOf(point));
	    }
	    
	    // read streets
	    XML ways[] = xml.getChildren("way");
	    for(XML way : ways)
	    {
	    	// read road type and name
	    	boolean isRoad = false;
	    	String name = "";
	    	XML tags[] = way.getChildren("tag");
	    	for(XML tag : tags)
	    		if(tag.hasAttribute("k") && tag.hasAttribute("v"))
	    		{
	    			if(tag.getString("k").equals("highway")) 
	    				switch(tag.getString("v"))
		    			{
		    			case "pedestrian":
		    			case "footway":
		    			case "cycleway":
		    			case "steps":
		    			case "path":
		    			case "living street":
		    				// isRoad = false; // initialized to false above
		    				break;
		    			default:
		    				isRoad = true;
		    			}
	    			else if(tag.getString("k").equals("name")) name = tag.getString("v");
	    		}
	    	if(!isRoad) continue;
	    	// read list of points
	    	ArrayList<Point> points = new ArrayList<Point>();
	    	XML nds[] = way.getChildren("nd");
	    	for(XML nd : nds)
	    	{
	    		long index = nd.getLong("ref", -1);
	    		Point nextPoint = allPoints.get((Integer)indexConvert.get(new Long(index)));
	    		nextPoint.isOnStreet = true;
	    		points.add(nextPoint);
	    	}
	    	// create new street
			Street street = new Street(points, name);
	    	allStreets.add(street);
	    	// add neighboring nodes to each node
	    	if(points.size() > 1) points.get(0).neighbors.add(points.get(1));
	    	for(int i=1;i<points.size()-1;i++)
	    	{
	    		points.get(i).neighbors.add(points.get(i-1));
	    		points.get(i).neighbors.add(points.get(i+1));
	    	}
	    	if(points.size() > 1) points.get(points.size()-1).neighbors.add(points.get(points.size()-2));
	    }
	    
	    // remove unused points from allPoints
	    ArrayList<Point> remPoints = new ArrayList<Point>();
	    for(Point point : allPoints)
	    	if(!point.isOnStreet) remPoints.add(point);
	    for(Point point : remPoints)
	    	allPoints.remove(point);
	}

	private XML openXML(String mapFileName) {
		return applet.loadXML(mapFileName);
	}

	private void initializePointsAndStreets() {
		allPoints = new ArrayList<Point>();
		allStreets = new ArrayList<Street>();
	}
	
	/*
	 * Sets the start and end GUI points to an initial state.
	 * The points are set apart to allow easier usability.
	 */
	private void initializeGuiPoints() {
		guiStart = new Point(applet.width * 2 / 10, applet.height / 2);
		guiEnd = new Point(applet.width * 8 / 10, applet.height / 2);
		guiDragging = null;
		moveEndPointsToClosestStreet();
	}
	
	public void clear()
	{
		allPoints.clear();
		allStreets.clear();
	}
	
	public void moveEndPointsToClosestStreet()
	{
		float dStart = Float.MAX_VALUE;
		float dEnd = Float.MAX_VALUE;
		float distSqr = Float.MAX_VALUE;
		
		// find closest point to start and end, save locations, and store mapStart & mapEnd
		for(Point point : allPoints)
		{
			if(point.x < 0 || point.x >= applet.width || point.y < 0 || point.y >= applet.height) continue;
			
			distSqr = (guiStart.x-point.x)*(guiStart.x-point.x)+(guiStart.y-point.y)*(guiStart.y-point.y);
			if(distSqr < dStart)
			{
				start = point;
				dStart = distSqr;
			}
			distSqr = (guiEnd.x-point.x)*(guiEnd.x-point.x)+(guiEnd.y-point.y)*(guiEnd.y-point.y);
			if(distSqr < dEnd)
			{
				end = point;
				dEnd = distSqr;
			}
		}
		
		// copy locations of closest points back into 
		guiStart.x = start.x;
		guiStart.y = start.y;
		guiEnd.x = end.x;
		guiEnd.y = end.y;		
		
		dirtyPoints = true;
	}	
}
