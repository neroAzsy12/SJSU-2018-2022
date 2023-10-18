/**
 * 
 * @author azsy
 *
 */
public class PartTimeHourlyEmployee extends Employee implements EmployeeBusiness{
	private float pay;	// hourly pay for the employee, no overtime is included
	
	public PartTimeHourlyEmployee() {
		
	}
	
	public PartTimeHourlyEmployee(String first, String last) {
		super(first, last);
	}
	
	public PartTimeHourlyEmployee(String first, String last, String name, String city, String state, int zip, int number) {
		super(first, last, name, city, state, zip, number);
	}
	
	public void setPay(float pay) {
		this.pay = pay;
	}
	public float getPay() {
		return this.pay;
	}
	
	public float computePay(int hours) {
		if(hours > 40) {
			return -1;
		}else {
			return pay * hours;
		}
	}
	
	@Override
	public String toString() {
		return "Part-time Employee: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.getID() 
		+ ", SSN: " + this.getSSN() + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit() + "\nIs paid $" + this.pay + "/hr.";
	}
	
	public void introduce(boolean flagSSN) {
		if(flagSSN) {
			System.out.println(this.toString());
		}else {
			System.out.println("Part-time Employee: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + ", ID#: " + this.getID() 
		    + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit() + "\nIs paid $" + this.pay + "/hr.");
		}
		
	}
}
