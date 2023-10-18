/**
 * @author azsy
 *
 */
public class StudentTest {
	public static void main(String[] args) {
		Student test = new Student();
		test.setAge(20);
		test.setFirst("John");
		test.setLast("Smith");
		test.setGpa(3.6f);
		test.setMajor("Computer Science major");
		test.setDepartment("School of Computer Science department");
		
		Student.Course t = test.new Course();	//non-static inner class
		t.addClase("M/W: CS 151 @ MQH 251");
		t.addClase("T/TH: Math 129A @ MQH 424");
		t.addClase("M/W: CS 147 @ DH 135");
		t.addClase("F: Kin 51A @ YUH 109");
		t.printSchedule();
	}
}
