/**
 * @author azsy
 *
 */
import java.io.*;
import java.util.*;
public class CreateFile {

	public static void main(String[] args) {
		Scanner sc = null; 
		File create = null;
		FileWriter write = null;
		BufferedWriter fileWriter = null;
		
		try {
			sc = new Scanner(System.in);
			create = new File("command_line_input.txt");
			write = new FileWriter(create);
			fileWriter = new BufferedWriter(write);
			
			String input = sc.nextLine();
			while(!input.equals("@q")) {
				fileWriter.write(input + "\n");
				input = sc.nextLine();
			}
			
			sc.close();
			fileWriter.close();
			write.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
