package routeFinder.model;

public class Point {
	
	private float x;
	private float y;
	
	public Point(float x, float y) {
		setX(x);
		setY(y);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Point) {
			Point otherPoint = (Point) other;
			return distanceTo(otherPoint) == 0;
		}
		return false;
	}

	public float getX() {
		return x;
	}

	protected void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	protected void setY(float y) {
		this.y = y;
	}
	
	public double distanceTo(Point other) {
		return Math.sqrt( sqr(this.x - other.x) + sqr(this.y - other.y) );
	}
	
	private double sqr(double value) {
		return Math.pow(value, 2);
	}
}
