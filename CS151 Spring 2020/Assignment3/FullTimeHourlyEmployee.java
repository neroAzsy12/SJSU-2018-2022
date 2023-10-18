/**
 * @author azsy
 *
 */
public class FullTimeHourlyEmployee extends Employee implements EmployeeBusiness{
	private float pay;						// hourly pay for the employee
	private final float overtime = 1.50f; 	// overtime rate for over 40 hrs is 1.5x more 
	
	public FullTimeHourlyEmployee() {
		
	}
	
	public FullTimeHourlyEmployee(String first, String last) {
		super(first, last);
	}
	
	public FullTimeHourlyEmployee(String first, String last, String name, String city, String state, int zip, int number) {
		super(first, last, name, city, state, zip, number);
	}
	
	public void setPay(float pay) {
		this.pay = pay;
	}
	public float getPay() {
		return this.pay;
	}
	
	// computes the pay, if the person works over 40 hrs, then make sure to include overtime pay
	// else if the person works <= 40 hrs then no need to calculate overtime pay 
	public float computePay(int hours) {
		if(hours > 40) {
			float totalPay = (40 * pay) + ((hours - 40) * overtime * pay);
			return totalPay;	// if overtime was done
		}else {
			return pay * hours;	// if no overtime was done
		}
	}
	
	@Override					// overrides the toString() method
	public String toString() {
		return "Full-time hourly Employee: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.getID() 
		+ ", SSN: " + this.getSSN() + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit() + "\nIs paid $" + this.pay + "/hr.";
	}
	
	public void introduce(boolean flagSSN) {
		if(flagSSN) {
			System.out.println(this.toString());
		}else {
			System.out.println("Full-time hourly Employee: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.getID() 
		    + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit() + "\nIs paid $" + this.pay + "/hr.");
		}
		
	}
}
