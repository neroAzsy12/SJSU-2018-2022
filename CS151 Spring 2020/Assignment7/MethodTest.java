/**
 * @author azsy
 *
 */
import java.util.*;
// Exercise #4
public class MethodTest {
	
	public static void main(String[] args) {
		 
		ArrayList<String> s = new ArrayList<>();
		s.add("Hello");
		s.add("World!");
		s.add("HI");
		s.add("YUP");
		s.add("net");
		
		ArrayList<Integer> i = new ArrayList<>();
		i.add(12);
		i.add(7);
		i.add(0);
		i.add(-990);
		i.add(100000);
		i.add(-1);
		
		ArrayList<Double> d = new ArrayList<>();
		d.add(1.90);
		d.add(-9.89);
		d.add(0.84);
		d.add(1000.21);
		d.add(-.98);
		d.add(-123.4);
		d.add(189.32);
		d.add(1.11);
		
		System.out.println(count(s));
		System.out.println(count(i));
		System.out.println(count(d));
	}
	
	public static <E> int count(ArrayList<E> genericLst) {
		int count = 0;
		for(E item : genericLst) {
			count++;
		}
		return count;
	}

}
