/**
 * @author azsy
 *
 */
public class StudentTest {
	public static void main(String[] args) {
		Student john = new Student();
		john.setFirst("John");
		john.setLast("Smith");
		john.setAge(20);
		john.setMajor("Computer Science");
		john.setDepartment("School of Computer Science department");
		john.setGPA(3.6f);
		
		Student cloned = null;
		
		try {
			cloned = (Student) john.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		john.printInfo();
		System.out.println();
		cloned.printInfo();
	}
}
