/**
 * @author azsy
 *
 */
public class Animal {
	private String name;
	private String type;
	private String gender;
	private int age;
	private String home;
	private float speed;
	
	public Animal() {
		this.name = " ";
		this.type = "none";
		this.gender = "unsure";
		this.age = 0;
		this.home = "no home";
		this.speed = 100000f;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name; 
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type; 
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return this.gender; 
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return this.age; 
	}
	
	public void setHome(String home) {
		this.home = home;
	}
	public String getHome() {
		return this.home; 
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getSpeed() {
		return this.speed; 
	}
	
	public String toString() {
		return this.type + " " + this.name + " " + this.age;
	}
}