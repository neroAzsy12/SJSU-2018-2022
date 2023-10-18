/**
 * @author azsy
 *
 */
public class BusinessTest {
	public static void main(String[] args) {
		Customer lily = new Customer();
		lily.setFirst("Lily");
		lily.setLast("Flower");
		lily.setAge(33);
		lily.setSSN("111-00-0000");
		lily.setAddress("Westside Dr", "Santa Cruz", "CA", 98740, 86);
		lily.setPayment("Credit Card");
		lily.setID(99999);
		lily.introduce();
		lily.makePayment();
		System.out.println();
		
		Customer bily = new Customer("Billy", "Pasta", "Spooner St", "Springfield", "SC", 72312, 101);
		bily.setPayment("Debit Card");
		bily.setAge(29);
		bily.setID(10992);
		bily.introduce();
		bily.makePayment();     
		System.out.println();
		
		PartTimeHourlyEmployee robert = new PartTimeHourlyEmployee("Robert", "Ramsey", "Cragemont Ave", "Fresno", "CA", 54312, 909);
		robert.setEducation("Bachelor's in Economics");
		robert.setDeposit("Direct Deposit");
		robert.setAge(34);
		robert.setID(78231);
		robert.setSSN("333-56-9090");
		robert.setPay(55f);
			
		robert.introduce(true);
		System.out.println(robert.computePay(40));
		System.out.println();
		
		PartTimeHourlyEmployee bob = new PartTimeHourlyEmployee("Bob", "Ridley");
		bob.setAge(25);
		bob.setSSN("346-89-0000");
		bob.setID(45678);
		bob.setAddress("Color Rd", "Los Angeles", "CA", 45721, 122);
		bob.setEducation("Master's in Business");
		bob.setPay(56f);
		bob.setDeposit("Check");
		
		bob.introduce(false);
		System.out.println(bob.computePay(41));
		System.out.println();
		
		FullTimeHourlyEmployee clause = new FullTimeHourlyEmployee();
		clause.setFirst("Clause");
		clause.setLast("Reddington");
		clause.setAge(45);
		clause.setSSN("234-78-2356");
		clause.setID(11221);
		clause.setAddress("Junction Rd", "Boston", "MA", 42091, 111);
		clause.setEducation("Master's in Computer Science");
		clause.setPay(65f);
		clause.setDeposit("Direct Deposit");
		
		clause.introduce(true);
		System.out.println(clause.computePay(50));
		System.out.println();
		
		FullTimeHourlyEmployee ronnie = new FullTimeHourlyEmployee("Ronnie", "Gustin", "Staples Rd", "Venice", "CA", 11111, 132);
		ronnie.setEducation("Master's in Biomechanical Engineering");
		ronnie.setDeposit("Direct Deposit");
		ronnie.setAge(30);
		ronnie.setID(69696);
		ronnie.setSSN("020-00-1080");
		ronnie.setPay(40f);
		
		ronnie.introduce(false);
		System.out.println(ronnie.computePay(39));
		System.out.println();
		
		Contractor d = new Contractor("Robert", "Ramsey", "Cragemont Ave", "Fresno", "CA", 54312, 909);
		d.setEducation("Bachelor's in Economics");
		d.setDeposit("Direct Deposit");
		d.setAge(34);
		d.setID(78231);
		d.setSSN("333-56-9090");
		d.setPay(55f);
		
		d.introduce(true);
		System.out.println(d.computePay(40));
		System.out.println();
		
		Contractor e = new Contractor();
		e.setFirst("Bob");
		e.setLast("Ridley");
		e.setAge(25);
		e.setSSN("346-89-0000");
		e.setID(45678);
		e.setAddress("Color Rd", "Los Angeles", "CA", 45721, 122);
		e.setEducation("Master's in Business");
		e.setPay(56f);
		e.setDeposit("Check");
		
		e.introduce(false);
		System.out.println(e.computePay(41));
		System.out.println();
		
		FullTimeSalaryEmployee oliver = new FullTimeSalaryEmployee("Oliver", "Azul", "1st St", "San Francisco", "CA", 13411, 102);
		oliver.setEducation("Master's in Bioinformatics");
		oliver.setDeposit("Direct Deposit");
		oliver.setAge(36);
		oliver.setID(69900);
		oliver.setSSN("020-00-1000");
		oliver.setPay(66f);
		
		oliver.introduce(true);
		System.out.println(oliver.computePay(39));
		System.out.println();
		
		FullTimeSalaryEmployee o = new FullTimeSalaryEmployee("Oliver", "Azul", "1st St", "San Francisco", "CA", 13411, 102);
		o.setEducation("Master's in Bioinformatics");
		o.setDeposit("Direct Deposit");
		o.setAge(36);
		o.setID(69900);
		o.setSSN("020-00-1000");
		o.setPay(66f);
		
		o.introduce(false);
		System.out.println(o.computePay(39));
		System.out.println();
		
		Executive a = new Executive("Raymond", "Reddington");
		a.setAge(40);
		a.setSSN("346-89-0000");
		a.setAddress("Color Rd", "Los Angeles", "CA", 45721, 122);
		a.setEducation("Master's in Business");
		a.setSalary(120000f);
		a.setDeposit("Direct Deposit");
		
		a.introduce(true, true);
		System.out.println(a.computePay());
		System.out.println();
		
		Executive b = new Executive();
		b.setFirst("Barry");
		b.setLast("Allen");
		b.setAge(56);
		b.setSSN("456-89-0000");
		b.setAddress("Cold Rd", "Los Santos", "CA", 45621, 172);
		b.setEducation("Master's in Business");
		b.setSalary(109000f);
		b.setDeposit("Direct Deposit");
		
		b.introduce(false, false);
		System.out.println(b.computePay());
		
		
	}
	
}
