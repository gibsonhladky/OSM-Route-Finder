package routeFinder.model;
import java.util.ArrayList;

public class Street 
{
	public final ArrayList<Point> points;
	public final String name;
	
	public Street(ArrayList<Point> points, String name)
	{
		this.points = points;
		this.name = name;
	}	
}