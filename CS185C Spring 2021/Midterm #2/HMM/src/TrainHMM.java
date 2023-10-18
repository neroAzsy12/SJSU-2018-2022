import java.io.*;
import java.util.*;
public class TrainHMM {

    public static void trainHMM(String name, String filename, int states, int symbols, String saveFile) {
        try {
            ArrayList<String> training = new ArrayList<>();
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                training.add(line);
            }

            fr.close();
            br.close();

            System.out.println("Training HMM for " + name + " malware family...");
            HiddenMarkovModel hmm = new HiddenMarkovModel(states, symbols + 1);
            hmm.train(training);
            System.out.println("Finished Training HMM for " + name + " malware family...");

            System.out.println("-----------------------------------------------");
            System.out.println("Initial state probabilities...");
            double[] pi = hmm.getPI();
            for(int i = 0; i < pi.length; i++) {
                System.out.print(pi[i] + " ");
            }
            System.out.println();
            System.out.println("-----------------------------------------------");

            System.out.println("State Transition probabilites...");
            double[][] a = hmm.getA();
            for(int i = 0; i < a.length; i++) {
                for(int j = 0; j < a.length; j++) {
                    System.out.print(a[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("-----------------------------------------------");

            System.out.println("Observation probability");
            double[][] b = hmm.getB();
            for(int i = 0; i < b.length; i++) {
                for(int j = 0; j < 21; j++) {
                    System.out.print(b[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("-----------------------------------------------");

            System.out.println("Saving HMM");
            hmm.save(saveFile);
            System.out.println("Saved...");

            System.out.println("-----------------------------------------------");
            System.out.println("-----------------------------------------------");
            System.out.println();

        } catch (IOException e) {
            System.out.println("File: " + filename + " does not exist...");
        }
    }

    public static void main(String[] args) {
        int numStates = 4;
        int uniqueSymbols = 20;

        trainHMM("winwebsec", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/winwebsec/dataset/winwebsec_training.txt", numStates, uniqueSymbols, "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Saved HMMS/winwebsec-HMM-4to20.txt");
        trainHMM("zbot", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zbot/dataset/zbot_training.txt", numStates, uniqueSymbols, "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Saved HMMS/zbot-HMM-4to20.txt");
        trainHMM("zeroaccess", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zeroaccess/dataset/zeroaccess_training.txt", numStates, uniqueSymbols, "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Saved HMMS/zeroaccess-HMM-4to20.txt");
    }
}
