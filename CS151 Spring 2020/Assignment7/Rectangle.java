/**
 * @author azsy
 *
 */
public class Rectangle extends Shape{
	private final String name = "Rectangle";
	private double length; 
	private double width;
	
	public Rectangle(double l, double w) {
		this.length = l;
		this.width = w;
	}
	
	public void setLength(double l) {
		this.length = l;
	}
	public double getLength() {
		return this.length;
	}
	
	public void setWidth(double w) {
		this.width = w;
	}
	public double getWidth() {
		return this.width;
	}
	
	@Override
	public double computeArea() {
		return this.length * this.width;
	}
	
	@Override
	public String toString() {
		return "Shape: " + this.name + ", Length: " + this.length + ", Width: " + this.width;
	}
}
