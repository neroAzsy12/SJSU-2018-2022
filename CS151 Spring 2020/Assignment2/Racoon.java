/**
 * @author azsy
 *
 */
public class Racoon extends Animal{
	public Racoon() {
		super();
	}
	
	public void move() {
		System.out.println(super.getName() + " moved farther from you." );
	}
	
	public void sound() {
		System.out.println("Hissss");
	}
	
	public void eat() {
		System.out.println("I am the raider of trash cans, I will find anything edible and eat it.");
	}
	
	public void sleep() {
		System.out.println("I sleep for at least 2 hours a day.");
		System.out.println("Afterwards, I will raid the trashcan and eat anything that is edible");
	}
	
	public void scratch() {
		System.out.println("As long as you let me be, you won't get scratched.");
		System.out.println("Also don't pick me up, or I will scratch you.");
	}
	
	public void run() {
		System.out.println("I can run at " + super.getSpeed()  + " miles/hr.");
	}
	
	public String toString() {
		return super.getName() + " is " + super.getAge() + " years old and is a " + super.getType() + " racoon.";
	}
}
