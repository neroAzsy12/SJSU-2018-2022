/**
 * @author azsy
 *
 */
public class Triangle extends Shape{
	private final String name = "Triangle";
	private double length;
	private double height;
	
	public Triangle(double l, double h) {
		this.length = l;
		this.height = h;
	}
	
	public void setLength(double l) {
		this.length = l;
	}
	public double getLength() {
		return this.length;
	}
	
	public void setHeight(double h) {
		this.height = h;
	}
	public double getHeight() {
		return this.height;
	}
	
	@Override
	public double computeArea() {
		return (this.height * this.length) / 2;
	}
	
	
	@Override
	public String toString() {
		return "Shape: " + this.name + ", Length: " + this.length + ", Height: " + this.height;
	}

}
