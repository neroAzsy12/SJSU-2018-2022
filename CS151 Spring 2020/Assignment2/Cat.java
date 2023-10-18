/**
 * @author azsy
 *
 */
public class Cat extends Animal implements Domesticated {
	public Cat() {
		super();
	}
	
	public void walk() {
		System.out.println("Do not walk me like a dog.");
		System.out.println("I will walk when I want to walk, without a le");
	}
	
	public void greetHuman() {
		System.out.println("Hello, my name is " + super.getName() + " and I am a " + super.getType() + " cat.");
	}
	
	public void bath() {
		System.out.println("Please don't give me a bath. I don't like baths.");
		System.out.println("I will attempt to escape the bath as soon as I touch the water.");
	}
	
	public void move() {
		System.out.println(super.getName() + " moved closer to you." );
	}
	
	public void sound() {
		System.out.println("Meow Meow");
	}
	
	public void eat() {
		System.out.println("Feed me fish, and give me milk to drink.");
	}
	
	public void sleep() {
		System.out.println("I sleep for at least 12 hours a day.");
		System.out.println("At most I'll sleep for 20 hours then be awake at night.");
	}
	
	public void scratch() {
		System.out.println("Feed me, or you'll get scratched.");
		System.out.println("Also don't pick me up, or I will scratch you.");
	}
	
	public void run() {
		System.out.println("I can run at " + super.getSpeed()  + " miles/hr.");
		System.out.println("You see, running is not my thing, I would rather just want to nap.");
	}
	
	public String toString() {
		return super.getName() + " is " + super.getAge() + " years old and is a " + super.getType() + " cat.";
	}
}
