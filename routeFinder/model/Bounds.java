package routeFinder.model;

public class Bounds {
	public final float minLat;
	public final float minLon;
	public final float maxLat;
	public final float maxLon;
	public final float latRange;
	public final float lonRange;

	public Bounds(float minLat, float minLon, float maxLat, float maxLon) {
		this.minLat = minLat;
		this.minLon = minLon;
		this.maxLat = maxLat;
		this.maxLon = maxLon;
		this.latRange = maxLat - minLat;
		this.lonRange = maxLon - minLon;
	}
}