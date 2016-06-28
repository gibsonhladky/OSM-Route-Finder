import java.util.ArrayList;

/*
 * Representation of a point on a map.
 */
public class Point 
{
	// x and y screen coordinate
	public float x;
	public float y;
	// list of points connected to this one by roads
	public ArrayList<Point> neighbors;
	// temporary variable used to cull unused points
	public boolean isOnStreet; 

	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
		neighbors = new ArrayList<Point>();
		isOnStreet = false;
	}	
}