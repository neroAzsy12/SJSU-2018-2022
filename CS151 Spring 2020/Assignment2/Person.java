/**
 * @author azsy
 * Date: 02/08/20
 */
public class Person {
	private String first;
	private String last;
	private int age;
	private String ssn;
	private String address;
	private String gender;
	private float weight;
	
	public Person() {
		this.first = "none";
		this.last = "none";
		this.age = 0;
		this.ssn = "XXX-XX-XXXX";
		this.address = "nowhere";
		this.gender = "blank";
		this.weight = 0.0f;	
	}
	public void setFirst(String first) {
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
	
	public void setSSN(String ssn) {
		this.ssn = ssn;
	}
	public String getSSN() {
		return this.ssn;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return this.address;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return this.gender;
	}
	
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getWeight() {
		return this.weight;
	}
	
	public void introduce() {
		System.out.println("Hello, my name is " + this.first + " " + this.last + ", and I am a " + this.age + " year old " + this.gender + " and I weigh " + this.weight + " lbs.");
		System.out.println("I live in " + this.address + " and my Social Security Number is " + this.ssn + ".");
	}
	
	public String toString() {
		return this.first + " " + this.last;
	}
}
