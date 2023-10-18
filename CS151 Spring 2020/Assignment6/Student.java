/**
 * @author azsy
 * 
 */
import java.util.*;
public class Student {
	private String first;
	private String last; 
	private int age; 
	private float gpa; 
	private String major;
	private String department;
	private LinkedList<Course> courses;
	
	public Student() {
		this.courses = new LinkedList<>();
	}
	
	public Student(String first, String last, int age, float gpa, String major, String department) {
		this.courses = new LinkedList<>();
		this.first = first;
		this.last = last;
		this.age = age;
		this.gpa = gpa;
		this.major = major;
		this.department = department;
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
	
	public void addCourse(Course c) {
		courses.add(c);
	}
	
	public void removeCourse() {
		courses.remove();
	}
	
	public void sortCourses(boolean ascending,  String attribute) {
		attribute = attribute.toUpperCase();
		switch(attribute) {
			case "NAME":
				if(ascending) {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c1.getName().compareTo(c2.getName());
						}
					});
				}else {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c2.getName().compareTo(c1.getName());
						}
					});
				}
				break;
			
			case "DESCRIPTION":
				if(ascending) {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c1.getDescription().compareTo(c2.getDescription());
						}
					});
				}else {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c2.getDescription().compareTo(c1.getDescription());
						}
					});
				}
				break;
				
			case "DEPARTMENT":
				if(ascending) {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c1.getDepartment().compareTo(c2.getDepartment());
						}
					});
				}else {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c2.getDepartment().compareTo(c1.getDepartment());
						}
					});
				}
				break;
				
			case "TIME":
				if(ascending) {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c1.getTime().compareTo(c2.getTime());
						}
					});
				}else {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c2.getTime().compareTo(c1.getTime());
						}
					});
				}
				break;
				
			case "DAY":
				if(ascending) {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c1.getDay().compareTo(c2.getDay());
						}
					});
				}else {
					Collections.sort(courses, new Comparator<Course>() {
						public int compare(Course c1, Course c2) {
							return c2.getDay().compareTo(c1.getDay());
						}
					});
				}
				break;
				
			default:
				System.out.println("Valid attribute inputs as string: name, department, description, time, day");
		}
		for(Course c : courses) {
			System.out.println(c.toString());
		}
	}
}
