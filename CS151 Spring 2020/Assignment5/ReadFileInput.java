/**
 * @author azsy
 *
 */
import java.io.*;
public class ReadFileInput {

	public static void main(String[] args) {
		File access = null; 
		FileReader reader = null;
		BufferedReader fileRead = null;
		try {
			
			access = new File("quote.txt");
			reader = new FileReader(access);
			fileRead = new BufferedReader(reader);
			
			String output = fileRead.readLine();
			while(output != null) {
				System.out.println(output);
				output = fileRead.readLine();
			}
			
			fileRead.close();
			reader.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
