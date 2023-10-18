import java.io.*;
import java.util.*;
public class TestHMM {
    public static ArrayList<String> readFile(String filename) {
        try {
            ArrayList<String> testing = new ArrayList<>();
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                testing.add(line);
            }

            fr.close();
            br.close();
            return testing;
        } catch (IOException e) {
            System.out.println("Unable to access " + filename);
            return null;
        }
    }

    public static void resultsMinMax(String name, double[] results) {
        double min = 0;
        double max = -Double.MAX_VALUE;

        for(double score : results) {
            if(score < min) {
                min = score;
            }

            if(score > max) {
                max = score;
            }
        }

        System.out.println("-----------------------------------");
        System.out.println(name + " results:");
        System.out.println("Min: " + min + ", Max: " + max);
    }

    public static String pair(double result, int val) {
        StringBuilder sb = new StringBuilder();
        sb.append(val);
        sb.append(',');
        sb.append(result);
        sb.append('\n');

        return sb.toString();
    }

    public static void saveResults(double[] r1, double[] r2, String filename) {
        try {
            FileWriter save = new FileWriter(filename);
            for(double r : r1) {
                save.write(pair(r, 0));
            }

            for(double r : r2) {
                save.write(pair(r, 1));
            }

            save.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testHMM(String name, String[] filenames, String hmmFile, String[] names) {
        HiddenMarkovModel hmm = new HiddenMarkovModel(hmmFile);

        ArrayList<String> test_one = readFile(filenames[0]);
        ArrayList<String> test_two = readFile(filenames[1]);
        ArrayList<String> test_three = readFile(filenames[2]);
        ArrayList<String> test_four = readFile(filenames[3]);

        System.out.println("Log scores for the " + name + " HMM");

        double[] results = hmm.test(test_one);
        resultsMinMax(name + " training set", results);

        double[] results2 = hmm.test(test_two);
        resultsMinMax(name + " testing set", results2);

        double[] results3 = hmm.test(test_three);
        if (name.equals("winwebsec")) {
            resultsMinMax("zbot testing set", results3);
        } else {
            resultsMinMax("winwebsec testing set", results3);
        }

        double[] results4 = hmm.test(test_four);
        if (name.equals("zeroaccess")) {
            resultsMinMax("zbot testing set", results4);
        } else {
            resultsMinMax("zeroaccess testing set", results4);
        }

        String f1 = "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/HMM Scores/" + name + "HMM_" + names[0];
        String f2 = "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/HMM Scores/" + name + "HMM_" + names[1];

        saveResults(results2, results3, f1);
        saveResults(results2, results4, f2);
    }

    public static void main(String[] args) {
        String[] winwebsec_files = {"/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/winwebsec/dataset/winwebsec_training.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/winwebsec/dataset/winwebsec_testing.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/winwebsec/dataset/zbot_testing.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/winwebsec/dataset/zeroaccess_testing.txt"};
        String[] zbot_files = {"/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zbot/dataset/zbot_training.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zbot/dataset/zbot_testing.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zbot/dataset/winwebsec_testing.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zbot/dataset/zeroaccess_testing.txt"};
        String[] zeroaccess_files = {"/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zeroaccess/dataset/zeroaccess_training.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zeroaccess/dataset/zeroaccess_testing.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zeroaccess/dataset/winwebsec_testing.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Data/zeroaccess/dataset/zbot_testing.txt"};

        String[] w = {"winwebsec vs zbot.txt", "winwebsec vs zeroaccess.txt"};
        String[] z = {"zbot vs winwebsec.txt", "zbot vs zeroaccess.txt"};
        String[] zz = {"zeroaccess vs winwebsec.txt", "zeroaccess vs zbot.txt"};

        testHMM("winwebsec", winwebsec_files, "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Saved HMMS/winwebsec-HMM-4to24.txt", w);
        System.out.println();

        testHMM("zbot", zbot_files, "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Saved HMMS/zbot-HMM-4to24.txt", z);
        System.out.println();

        testHMM("zeroaccess", zeroaccess_files, "/Users/azsy/Desktop/Jupyter NB/CS185C/Midterm #2/Saved HMMS/zeroaccess-HMM-4to24.txt", zz);
    }
    // rerun for 20
}
