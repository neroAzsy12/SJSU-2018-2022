/**
 * @author azsy
 *
 */
public class Customer extends Person{
	private int ID;			// id number for customer
	private String payment; // payment preference method
	
	public Customer() {
	}
	
	public Customer(String first, String last) {
		super(first, last);
	}
	
	public Customer(String first, String last, String name, String city, String state, int zip, int number) {
		super(first, last, name, city, state, zip, number);
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	public int getID() {
		return this.ID;
	}
	
	public void setPayment(String pay) {
		this.payment = pay;
	}
	public String getPayment() {
		return this.payment; 
	}
	
	public void makePayment() {
		System.out.println("My preferred method of payment is " + this.payment + ".");
	}
	
	@Override
	public String toString() {
		return "Customer: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.ID + ", Preferred Payment Method: " + this.payment + "\nLives in: " + this.getAddress(); 
	}
	
	public void introduce() {
		System.out.println(this.toString());
	}
	
}
