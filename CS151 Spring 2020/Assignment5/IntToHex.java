/**
 * @author azsy
 *
 */
import java.util.*;
public class IntToHex {

	public static void main(String[] args) {
		// biggest positive int is 2147483647
		// biggest negative int is -2147483648
		Scanner sc = new Scanner(System.in);
		try{
			int num = sc.nextInt();
			System.out.println(Integer.toHexString(num)); //built in library function, that converts int to hex representation, also takes into account neg #'s
			sc.close();
		}catch(InputMismatchException e) {
			System.out.println("Input must be a 32-bit integer value...");
		}
	}

}
