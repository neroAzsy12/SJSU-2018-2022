/**
 * @author azsy
 *
 */
public class Person {
	private String first;
	private String last;
	private int age;
	
	public Person() {
		
	}
	
	public Person(String first, String last, int age) {
		this.first = first;
		this.last = last;
		this.age = age;
	}
	
	public void setFrist(String first) {
		this.first = first;
	}
	public String getFirst() {
		return this.first;
	}
	
	public void setLast(String last) {
		this.last = last;
	}
	public String getLast() {
		return this.last;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return this.age;
	}
	
	@Override
	public String toString() {
		return this.first + " " + this.last + ", Age: " + this.age;
	}
}
