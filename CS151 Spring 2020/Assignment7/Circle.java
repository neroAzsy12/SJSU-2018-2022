/**
 * @author azsy
 *
 */
public class Circle extends Shape{
	private final String name = "Circle";
	private double radius;
	
	public Circle(double r) {
		this.radius = r;
	}
	
	public void setRadius(double r) {
		this.radius = r;
	}
	public double getRadius() {
		return this.radius;
	}
	
	@Override
	public double computeArea() {
		return Math.PI * Math.pow(this.radius, 2);
	}
	
	@Override 
	public String toString() {
		return "Shape: " + this.name + ", Radius: " + this.radius;
	}
}
