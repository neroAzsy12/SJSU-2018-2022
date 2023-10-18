/**
 * @author azsy
 * Exercise 1
 */
import java.util.*;
public class Student {
	private String first;
	private String last;
	private int age;
	private float gpa;
	private String major;
	private String department;

	public Student() {
	}
	
	// Setter and Getter for First name
	public void setFirst(String name) {
		this.first = name;
	}
	public String getFirst() {
		return this.first;
	}
	
	// Setter and Getter for Last name
	public void setLast(String name) {
		this.last = name;
	}
	public String getLast() {
		return this.last;
	}
	
	// Setter and Getter for Major
	public void setMajor(String major) {
		this.major = major;
	}
	public String getMajor() {
		return this.major;
	}
	
	// Setter and Getter for department
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartment() {
		return this.department;
	}
	
	// Setter and Getter for Age
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return this.age;
	}
	
	// Setter and Getter for GPA
	public void setGpa(float gpa) {
		this.gpa = gpa;
	}
	public float getGpa() {
		return this.gpa;
	}
	
	public String toString() {
		return this.first + " " + last + ", " + this.age + " year old, " + this.gpa + " gpa, " + this.major + ", " + this.department;
	}
	
	class Course{
		private ArrayList<String> classes= new ArrayList<>();
		void addClase(String clase) {
			classes.add(clase);
		}
		void printSchedule() {
			if(first.endsWith("s")) {
				System.out.println(first + "' schedule:");
			}else {
				System.out.println(first + "'s schedule:");
			}
			for(String c : classes) {
				System.out.println(c);
			}
		}
	}
}
