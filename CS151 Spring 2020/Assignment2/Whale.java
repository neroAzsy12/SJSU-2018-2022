/**
 * @author azsy
 *
 */
public class Whale extends Animal{
	public Whale() {
		super();
	}
	
	public void eat() {
		System.out.println("I eat: Squid, Krill, and Plankton.");
		System.out.println("I eat a lot since I'm very big.");
	}
	
	public void move() {
		System.out.println("Just keep swimming, just keep swimming.");
	}
	
	public void sleep() {
		System.out.println("I only sleep at minutes at a time, so I don't drown while sleeping.");
	}
	
	public void sound() {
		System.out.println("Insert Whale sounds.");
	}
	
	public void swim() {
		System.out.println(super.getName() + " loves to swim by the " + super.getHome());
		System.out.println(super.getName() + " can swim at " + super.getSpeed() + " miles/hr.");
		
	}
	
	public String toString() {
		return super.getName() + " is " + super.getAge() + " years old and is a " + super.getType();
	}
}
