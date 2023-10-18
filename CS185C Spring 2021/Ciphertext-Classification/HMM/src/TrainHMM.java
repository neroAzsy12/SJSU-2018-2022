import java.util.*;
import java.io.*;

public class TrainHMM {

    public static void trainHMM(String name, String filename, int states, int symbols, String saveFile) {
        try {
            StringBuilder sb = new StringBuilder();
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            //System.out.println(sb.length());

            fr.close();
            br.close();

            System.out.println("Training " + name + " HMM");
            HiddenMarkovModel hmm = new HiddenMarkovModel(states, symbols);
            hmm.train(sb.toString().toCharArray(), 100, 1000);

            System.out.println("Finished Training");
            hmm.save(saveFile);
            System.out.println(name + " HMM has been saved to: " + saveFile);

        } catch (IOException e) {
            System.out.println("File: " + filename + " does not exist...");
        }
    }

    public static void main(String[] args) {
        //trainHMM("English Language", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/PlainText.txt", 2, 26, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/EnglishHMM.txt");
        //trainHMM("SimpleSubstitution", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/SimpleSub.txt", 2, 26, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/SimpleSubHMM.txt");
        //trainHMM("ColumnarTransposition", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/ColumnarTrans.txt", 2, 26, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/ColumnarTransHMM.txt");
        //trainHMM("Vigenere", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/Vigenere.txt", 2, 26, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/VigenereHMM.txt");
        //trainHMM("PlayFair", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/PlayFair.txt", 2, 26, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/PlayFairHMM.txt");
        trainHMM("FourSquare", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/FourSquare.txt", 2, 26, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/FourSquareHMM.txt");

        System.out.println("Working");
    }
}
