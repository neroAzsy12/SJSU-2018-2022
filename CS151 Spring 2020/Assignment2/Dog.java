/**
 * @author azsy
 *
 */
public class Dog extends Animal implements Domesticated{
	public Dog() {
		super();
	}
	
	public void walk() {
		System.out.println("I love walks!!!");
		System.out.println("I always want to go on walks.");
	}
	
	public void greetHuman() {
		System.out.println("Hello, my name is " + super.getName() + " and I am a " + super.getType());
	}
	
	public void bath() {
		System.out.println("I like baths, especially when I shake in order to get dry.");
	}
	
	public void move() {
		System.out.println(super.getName() + " went to bury a bone.");
	}
	
	public void sound() {
		System.out.println("Woof");
	}
	
	public void eat() {
		System.out.println("I love peanut butter, but it gets stuck in my mouth.");
		System.out.println("Please don't give me chocolate.");
	}
	
	public void sleep() {
		System.out.println("I sleep for around 10 hours a day");
		System.out.println("When I wake up, I have so much energy and want to play.");
	}
	
	public void bark() {
		System.out.println("Woof Woof Woof Woof Woof Woof Woof");
	}
	
	public void run() {
		System.out.println("I can run at " + super.getSpeed()  + " miles/hr.");
	}
	
	public String toString() {
		return super.getName() + " is " + super.getAge() + " years old and is a " + super.getType();
	}
}
