/**
 * @author azsy
 *
 */
public class AnimalTest {

	public static void main(String[] args) {
		Whale willy = new Whale();
		willy.setName("Willy");
		willy.setAge(10);
		willy.setGender("male");
		willy.setHome("Atlantic Ocean");
		willy.setType("Orca");
		willy.setSpeed(12.1f);
		
		Racoon rigby = new Racoon();
		rigby.setName("Rigby");
		rigby.setAge(8);
		rigby.setGender("male");
		rigby.setHome("Walnut Creek");
		rigby.setType("Ring tailed");
		rigby.setSpeed(0.1f);
		
		Cat al = new Cat();
		al.setName("Whiskers");
		al.setAge(2);
		al.setGender("male");
		al.setHome("Your bed");
		al.setSpeed(2.9f);
		al.setType("calico");
		
		Dog p = new Dog();
		p.setName("Pluto");
		p.setAge(5);
		p.setGender("male");
		p.setHome("Dog House");
		p.setSpeed(3.3f);
		p.setType("German Shepard");
		
		willy.swim();
		System.out.println();
		System.out.println(rigby.toString());
		System.out.println();
		al.greetHuman();
		System.out.println();
		p.bark();
	}

}
