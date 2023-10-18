/**
 * @author azsy
 *
 */
public class Hexagon extends Shape{
	private final String name = "Hexagon";
	private double side;
	
	public Hexagon(double side) {
		this.side = side;
	}
	
	public void setSide(double side) {
		this.side = side;
	}
	public double getSide() {
		return this.side;
	}
	
	@Override
	public double computeArea() {
		return (3 * Math.sqrt(3) / 2) * Math.pow(this.side,  2);
	}
	
	@Override
	public String toString() {
		return "Shape: " + this.name + ", Side: " + this.side;
	}
}
