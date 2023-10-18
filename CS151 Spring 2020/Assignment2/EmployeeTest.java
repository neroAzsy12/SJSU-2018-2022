/**
 * @author azsy
 *
 */
public class EmployeeTest {
	public static void main(String[] args) {
		Employee a = new Employee();
		a.setFirst("Joe");
		a.setLast("Smith");
		a.setAge(21);
		a.setSSN("111-11-1111");
		a.setAddress("121 Brightbrun Ave.");
		a.setGender("male");
		a.setWeight(123.90f);
		a.setID("1234");
		a.setStatus("contractor");
		a.setStartDate("12/09/19");
		a.setSalary("$60");
		
		Employee b = new Employee();
		b.setFirst("Lisa");
		b.setLast("Gray");
		b.setAge(40);
		b.setSSN("111-11-1112");
		b.setAddress("190 Spooner Ave");
		b.setGender("female");
		b.setWeight(135f);
		b.setID("0000");
		b.setStatus("full time employee");
		b.setStartDate("01/12/12");
		b.setSalary("$110,000");
		
		Employee c = new Employee();
		c.setFirst("Timothy");
		c.setLast("Brigs");
		c.setAge(42);
		c.setSSN("111-11-1113");
		c.setAddress("192 Spooner Ave");
		c.setGender("male");
		c.setWeight(165f);
		c.setID("0001");
		c.setStatus("full time employee");
		c.setStartDate("02/12/16");
		c.setSalary("$80,000");
		
		Employee d = new Employee();
		d.setFirst("George");
		d.setLast("Wallace");
		d.setAge(32);
		d.setSSN("111-11-9999");
		d.setAddress("86 Bottle Rd");
		d.setGender("male");
		d.setWeight(149.7f);
		d.setID("9898");
		d.setStatus("part time employee");
		d.setStartDate("01/12/20");
		d.setSalary("$20");
		
		Employee e = new Employee();
		e.setFirst("Amy");
		e.setLast("Student");
		e.setAge(28);
		e.setSSN("111-11-1115");
		e.setAddress("32 Walnut Ln");
		e.setGender("female");
		e.setWeight(129.4f);
		e.setID("1123");
		e.setStatus("contractor employee");
		e.setStartDate("08/24/18");
		e.setSalary("$45");
		
		a.introduce();
		System.out.println();
		b.introduce();
		System.out.println();
		c.introduce();
		System.out.println();
		d.introduce();
		System.out.println();
		e.introduce();
		System.out.println();
		
		a.calculatePay(30);
		System.out.println();
		b.calculatePay(2);
		System.out.println();
		c.calculatePay(4);
		System.out.println();
		d.calculatePay(25);
		System.out.println();
		e.calculatePay(45);
		System.out.println();
	}
}
