/**
 * @author azsy
 * 
 */
public class Employee {
	private String first;
	private String last; 
	private int ID;
	private float pay;
	
	public Employee() {
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
	
	public void setID(int id) {
		this.ID = id; 
	}
	public int getID() {
		return this.ID; 
	}
	
	public void setPay(float pay) {
		this.pay = pay;
	}
	public float getPay() {
		return this.pay; 
	}
	
	public float computePay(int hours){
		try {
			if(hours < 0){
				throw new NumberFormatException("Invalid number of hours. Has to be between 0 to 40 hours"); 
			}else if(hours >= 0 && hours <= 40) {
				return pay * hours;
			}else {
				throw new TooManyHoursWorkedException("Too many hours...");
			}
		}catch(NumberFormatException e) {
			System.out.println(e);
			return -1; // it was either -1 since it is invalid or actually calculate the pay*hours
			
		}catch(TooManyHoursWorkedException o) {
			System.out.println(o);
			return -1;
		}
	}
}
	