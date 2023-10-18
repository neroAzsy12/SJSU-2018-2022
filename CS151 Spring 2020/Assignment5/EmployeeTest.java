/**
 * @author azsy
 *
 */
public class EmployeeTest {
	public static void main(String[] args) {
		Employee test = new Employee();
		test.setFirst("John");
		test.setLast("Smith");
		test.setID(101);
		test.setPay(35f);
		
		System.out.println(test.computePay(40));
		System.out.println();
		System.out.println(test.computePay(23));
		System.out.println();
		System.out.println(test.computePay(1));
		System.out.println();
		System.out.println(test.computePay(0));
		System.out.println();
		System.out.println(test.computePay(-5));
		System.out.println();
		System.out.println(test.computePay(45));
		
	}
}
