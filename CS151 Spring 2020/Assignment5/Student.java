/**
 * @author azsy
 * 
 */
public class Student implements Cloneable {
	private String first;
	private String last; 
	private int age; 
	private float gpa; 
	private String major;
	private String department;
	
	private Course cs151;
	private Course cs123A;
	private Course cs147;
	private Course biol31;
	
	public Student() {
		this.cs151 = new Course("CS 151", "Object Oriented Design and Programming", "CS", "6:00pm", "Tue");
		this.cs123A = new Course("CS 123A", "Bioinformatics I", "CS", "10:30am", "Tue");
		this.cs147 = new Course("CS 147", "Computer Architecture", "CS", "7:30pm", "Thur");
		this.biol31 = new Course("BIOL 31", "Biology II", "BIO", "9:00am", "Mon");
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
	
	public void setGPA(float gpa) {
		this.gpa = gpa;
	}
	public float getGPA() {
		return this.gpa;
	}
	
	public void setMajor(String major) {
		this.major = major; 
	}
	public String getMajor() {
		return this.major; 
	}
	
	public void setDepartment(String department) {
		this.department = department; 
	}
	public String getDepartment() {
		return this.department;
	}
	
	public void printInfo() {
		System.out.println(this.getFirst() + " " + this.getLast() + ", Age: " + this.age + ", Major: " + this.getMajor() + ", Department: " + this.getDepartment() + ", GPA: " + this.getGPA());
		System.out.println("Courses for the semester:");
		System.out.println(cs151.getName() + ", " + cs151.getDescription() + ", " + cs151.getDepartment() + ", " + cs151.getTime() + ", " + cs151.getDay());
		System.out.println(cs123A.getName() + ", " + cs123A.getDescription() + ", " + cs123A.getDepartment() + ", " + cs123A.getTime() + ", " + cs123A.getDay());
		System.out.println(cs147.getName() + ", " + cs147.getDescription() + ", " + cs147.getDepartment() + ", " + cs147.getTime() + ", " + cs147.getDay());
		System.out.println(biol31.getName() + ", " + biol31.getDescription() + ", " + biol31.getDepartment() + ", " + biol31.getTime() + ", " + biol31.getDay());
	}
	
	protected Object clone() throws CloneNotSupportedException{
		Student s = (Student) super.clone();
		s.cs151 = (Course) cs151.clone();
		s.cs123A = (Course) cs123A.clone();
		s.cs147 = (Course) cs147.clone();
		s.biol31 = (Course) biol31.clone();
		return s;
	}
}
