/**
 * @author azsy
 *
 */
import java.util.*;
public class PersonTest {

	public static void main(String[] args) {
		Person a = new Person("Joe", "Smith", 40);
		Person b = new Person("Amy", "Gold", 32);
		Person c = new Person("Tony", "Stork", 21);
		Person d = new Person("Sean", "Irish", 24);
		Person e = new Person("Tina", "Brock", 28);
		Person f = new Person("Lenny", "Hep", 18);
		
		ArrayList<Person> peeps = new ArrayList<>(); 
		peeps.add(a);
		peeps.add(b);
		peeps.add(c);
		peeps.add(d);
		peeps.add(e);
		peeps.add(f);
		
		// sorting by age
		Collections.sort(peeps, new Comparator<Person>() {
			public int compare(Person a1, Person a2) {
				return a1.getAge() - a2.getAge();
			}
		});
		System.out.println("Sorted by age in ascending order: ");
		for(Person x : peeps) {
			System.out.println(x.toString());
		}
		
		System.out.println();
		
		Collections.sort(peeps, new Comparator<Person>() {
			public int compare(Person a1, Person a2) {
				return a2.getAge() - a1.getAge();
			}
		});
		System.out.println("Sorted by age in descending order: ");
		for(Person x : peeps) {
			System.out.println(x.toString());
		}
		
		System.out.println();
		
		// sorting by first name
		Collections.sort(peeps, new Comparator<Person>() {
			public int compare(Person a1, Person a2) {
				return a1.getFirst().compareTo(a2.getFirst());
			}
		});
		System.out.println("Sorted by first name in ascending order: ");
		for(Person x : peeps) {
			System.out.println(x.toString());
		}
		
		System.out.println();
		
		Collections.sort(peeps, new Comparator<Person>() {
			public int compare(Person a1, Person a2) {
				return a2.getFirst().compareTo(a1.getFirst());
			}
		});
		System.out.println("Sorted by first name in descending order: ");
		for(Person x : peeps) {
			System.out.println(x.toString());
		}
		
		System.out.println();
		
		// sorting by last name
		Collections.sort(peeps, new Comparator<Person>() {
			public int compare(Person a1, Person a2) {
				return a1.getLast().compareTo(a2.getLast());
			}
		});
		System.out.println("Sorted by last name in ascending order: ");
		for(Person x : peeps) {
			System.out.println(x.toString());
		}
		
		System.out.println();
		
		Collections.sort(peeps, new Comparator<Person>() {
			public int compare(Person a1, Person a2) {
				return a2.getLast().compareTo(a1.getLast());
			}
		});
		System.out.println("Sorted by last name in descending order: ");
		for(Person x : peeps) {
			System.out.println(x.toString());
		}
		
	}

}
