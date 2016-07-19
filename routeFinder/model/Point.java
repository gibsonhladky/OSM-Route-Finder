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
			return this.x == otherPoint.x && this.y == otherPoint.y;
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
		return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
	}
	
}
