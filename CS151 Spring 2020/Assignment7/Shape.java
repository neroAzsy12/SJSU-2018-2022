/**
 * @author azsy
 *
 */
import java.io.*;
import java.util.*;
abstract class Shape implements Serializable, Runnable {

	private ArrayList<Shape> shapeList;
	
	public Shape() {
		shapeList = new ArrayList<>();
		shapeList.add(this);
	}
	
	// setter and getter methods for shapeList
	public void setShapeList(ArrayList<Shape> s) {
		this.shapeList = s;
	}
	public ArrayList<Shape> getShapeList(){
		return this.shapeList;
	}
	
	// this helps during the serialization exercise
	public Shape get(int i) {
		return shapeList.get(i);
	}
	
	// adds a shape to ShapeList
	public void add(Shape s) {
		shapeList.add(s);
	}
	// removes the shape at index i
	public void remove(int i) {
		shapeList.remove(i);
	}
	
	// abstract method
	abstract public double computeArea();
	
	// synchronized method
	synchronized public void compute() {
		for(Shape s: shapeList) {
			System.out.println("Area: " + s.computeArea());
		}
	}
	
	// returns the max area of a shape in shapeList
	public Shape max() {
		double a = Double.MIN_VALUE;
		Shape tmp = null;
		
		for(Shape s: shapeList) {
			if(a < s.computeArea()) {
				a = s.computeArea();
				tmp = s;
			}
		}
		return tmp;
	}
	
	// returns the min area of a shape in shapeList
	public Shape min() {
		double b = Double.MAX_VALUE;
		Shape tmp = null;
		
		for(Shape s: shapeList) {
			if (b > s.computeArea()) {
				b = s.computeArea();
				tmp = s;
			}
		}
		return tmp;
	}
	
	public void run() {
		try {
			System.out.println("Current thread: " + Thread.currentThread().getId());
			this.compute();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void start() {
		Thread threadObj = new Thread(this);
		threadObj.start();
	}
}
