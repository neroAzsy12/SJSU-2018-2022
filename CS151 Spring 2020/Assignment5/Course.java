/**
 * @author azsy
 *
 */
public class Course implements Cloneable{
	private String name;
	private String description;
	private String department;
	private String time; 
	private String day; 
	
	public Course() {
	}
	
	public Course(String name, String description, String department, String time, String day) {
		this.name = name;
		this.description = description;
		this.department = department;
		this.time = time;
		this.day = day;
	}
	public void setName(String name) {
		this.name = name; 
	}
	public String getName() {
		return this.name; 
	}
	
	public void setDescription(String description) {
		this.description = description; 
	}
	public String getDescription() {
		return this.description;
	}
	
	public void setDepartment(String department) {
		this.department = department; 
	}
	public String getDepartment() {
		return this.department; 
	}
	
	public void setTime(String time) {
		this.time = time; 
	}
	public String getTime() {
		return this.time; 
	}
	
	public void setDay(String day) {
		this.day = day; 
	}
	public String getDay() {
		return this.day; 
	}
	
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
}
