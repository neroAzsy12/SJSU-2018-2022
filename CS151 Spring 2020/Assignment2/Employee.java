/**
 * @author azsy
 * Date: 02/08/20
 */
public class Employee extends Person{
	private String id;
	private String status;		
	private String startDate;
	private String salary;
	
	public Employee() {
		super();
		this.id = "XXXX";
		this.status = "unemployed";
		this.startDate = "laid off";
		this.salary = "none";
	}
	
	public void setID(String id) {
		this.id = id;
	}
	public String getID() {
		return this.id;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return this.status;
	}
	
	public void setStartDate(String date) {
		this.startDate = date;
	}
	public String getDate() {
		return this.startDate;
	}
	
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getSalary() {
		return this.salary;
	}
	
	public void introduce() {
		super.introduce();
		if(this.status.equals("full time employee")) {
			System.out.println("I am a " + this.status + " and get paid " + this.salary + " per year and my ID is " + this.id + " and I started on " + this.startDate + ".");
		}else {
			System.out.println("I am a " + this.status + " and get paid " + this.salary + " per hour and my ID is " + this.id + " and I started on " + this.startDate + ".");
		}
	}
	
	public void calculatePay(int time) {
		if(this.status.equals("full time employee")){
			System.out.println(super.getFirst() + " " + super.getLast() + ", a " + this.status + ", pay is " + this.salary + "/yr, should get paid or working " + time + " weeks.");
		}else {
			System.out.println(super.getFirst() + " " + super.getLast() + ", a " + this.status + ", pay is " + this.salary + "/hr, should get paid or working " + time + " hours.");
		}
	}
}
