/**
 * @author azsy
 * Date: 02/08/20
 */
public class Employee extends Person{
	private int ID;			  // id number for employee
	private String education; // level of education
	private String deposit;   // direct deposit or not
	
	public Employee() {
		
	}
	
	public Employee(String first, String last) {
		super(first, last);
	}
	
	public Employee(String first, String last, String name, String city, String state, int zip, int number) {
		super(first, last, name, city, state, zip, number);
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	public int getID() {
		return this.ID;
	}
	
	public void setEducation(String education) {
		this.education = education;
	}
	public String getEducation() {
		return this.education;
	}
	
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getDeposit() {
		return this.deposit;
	}
}
