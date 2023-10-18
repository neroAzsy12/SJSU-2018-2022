/**
 * @author azsy
 *
 */
public class FullTimeSalaryEmployee extends Employee implements EmployeeBusiness{
	private float pay; // hourly salary
	
	public FullTimeSalaryEmployee() {
		
	}
	
	public FullTimeSalaryEmployee(String first, String last) {
		super(first, last);
	}
	
	public FullTimeSalaryEmployee(String first, String last, String name, String city, String state, int zip, int number) {
		super(first, last, name, city, state, zip, number);
	}
	
	public void setPay(float p) {
		this.pay = p;
	}
	public float getPay() {
		return this.pay;
	}
	public float computePay(int weeks) {
		if(weeks > 0) {
			return pay * 40 * weeks;
		}else {
			return 0;
		}	
	}
	
	@Override					// overrides the toString() method
	public String toString() {
		return "Full-time Salary Employee: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.getID() 
		+ ", SSN: " + this.getSSN() + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit();
	}
	
	public void introduce(boolean flagSSN) {
		if(flagSSN) {
			System.out.println(this.toString());
		}else {
			System.out.println("Full-time Salary Employee: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.getID() 
		    + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit());
		}
		
	}
}
