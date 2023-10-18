/**
 * @author azsy
 *
 */
import java.io.*;
public class ShapeTest {

	public static void main(String[] args) throws IOException {
		// Shape s = new Shape(); this leads to a compile error
		Shape s = new Circle(12); // makes a shape type, that has a circle object included in shapeList
		s.add(new Circle(32));
		s.add(new Triangle(2, 5));
		s.add(new Triangle(5, 6));
		s.add(new Hexagon(30));
		s.add(new Hexagon(1));
		s.add(new Rectangle(34, 1090));
		s.add(new Rectangle(12, 190));
		
		for(int i = 0; i < 10; i++) {
			s.start();
		}
		System.out.println();
		
		// Exercise #2
		System.out.println(s.max().toString());
		System.out.println(s.min().toString());
		
		// Exercise #3
		try {
			FileOutputStream streamOut = new FileOutputStream("./obj1.ser");
			ObjectOutputStream objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(0));
			
			streamOut = new FileOutputStream("./obj2.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(1));
			
			streamOut = new FileOutputStream("./obj3.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(2));
			
			streamOut = new FileOutputStream("./obj4.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(3));
			
			streamOut = new FileOutputStream("./obj5.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(4));
			
			streamOut = new FileOutputStream("./obj6.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(5));
			
			streamOut = new FileOutputStream("./obj7.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(6));
			
			streamOut = new FileOutputStream("./obj8.ser");
			objectOutput = new ObjectOutputStream(streamOut);
			objectOutput.writeObject(s.get(7));
			
			objectOutput.close();
			streamOut.close();
		}catch (IOException e) {
			System.out.println(e);
		}
	}
}