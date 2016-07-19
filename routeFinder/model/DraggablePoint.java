package routeFinder.model;

public class DraggablePoint extends Point {

	public DraggablePoint(float x, float y) {
		super(x, y);
	}
	
	public void dragTo(float x, float y) {
		setX(x);
		setY(y);
	}

}
