/**
 * @author azsy
 * Date: 02/24/20
 */
public class Person {
	private String first; 	 // first name
	private String last;  	 // last name
	private int age;		 // age
	private String ssn;	     // social security number
	private Address address; // address class 
	
	public Person() {			// default constructor will also initialize address with its default
		address = new Address();
	}
	
	public Person(String first, String last) {
		this.first = first;
		this.last = last;
	}
	
	public Person(String first, String last, String name, String city, String state, int zip, int number) {
		this.first = first;
		this.last = last;
		address = new Address(name, city, state, zip, number);
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
	
	public void setAddress(String name, String city, String state, int zip, int number) {
		address = new Address(name, city, state, zip, number);
	}
	public String getAddress() {
		return address.info();
	}
	
	// inner class, since a Person has an Address
	private class Address{
		private String name;	// Street name
		private String city;	// city name
		private String state;	// state name
		private int zip;		// zip code
		private int number;		// address number
		
		private Address() {		
		}
	
		private Address(String name, String city, String state, int zip, int number) {
			this.name = name;
			this.city = city;
			this.state = state;
			this.zip = zip;
			this.number = number;
		}
		
		// returns the info of the address
		private String info() {
			if(city == null) {
				return "no address";	// if city is null then it returns "no address"
			}else {
				return this.number + " " + this.name + ", " + this.city + ", " + this.state + ", " + this.zip;
		
			}
		}
	}
}
