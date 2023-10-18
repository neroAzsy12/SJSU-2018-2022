/**
 * @author azsy
 *
 */
public class Executive extends Person{
	private String education;
	private String deposit;
	private float salary;				// yearly salary 
	private float bonus = 0.15f;	// yearly bonus
	
	public Executive() {
		
	}
	
	public Executive(String first, String last) {
		super(first, last);
	}
	
	public Executive(String first, String last, String name, String city, String state, int zip, int number) {
		super(first, last, name, city, state, zip, number);
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
	
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public float getSalary() {
		return this.salary;
	}
	
	public float computePay() {
		this.bonus *= salary;
		return salary + bonus;
	}
	
	@Override
	public String toString() {
		return "Executive: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() +  ", SSN: " + this.getSSN() 
		+ "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit() + "\nEarns $" + this.salary + "/yr.";
	}
	
	public void introduce(boolean flagSSN, boolean flagPay) {
		if(flagSSN && flagPay) {
			System.out.println(this.toString());
		
		}else if(flagSSN) {
			System.out.println("Executive: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() +  
		    ", SSN: " + this.getSSN() + "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit());
		
		}else if(flagPay) {
			System.out.println("Executive: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() +
		    "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit() + "\nEarns $" + this.salary + "/hr.");
		
		}else {
			System.out.println("Executive: " + this.getFirst() + " " + this.getLast() + ", Age: " + this.getAge() + 
		    "\nLives in: " + this.getAddress() + "\nEducation: " + this.getEducation() + ", Preferrence: " + this.getDeposit());
		}
	}
	
}
