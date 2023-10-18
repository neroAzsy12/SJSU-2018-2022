import java.util.*;
import java.io.*;
public class TestHMM {

    public static double indexOfCoincidence(String text) {
        int N = text.length();
        HashMap<Character, Integer> frequency = new HashMap<>();

        for(char c : text.toCharArray()) {
            if(frequency.containsKey(c)) {
                frequency.put(c, frequency.get(c) + 1);
            } else {
                frequency.put(c, 1);
            }
        }

        double freq_sum = 0;
        for(char c : frequency.keySet()) {
            freq_sum += (frequency.get(c) * (frequency.get(c) - 1));
        }

        return (freq_sum / (N * (N - 1)));
    }

    public static double shannonEntropy(String text) {
        int N = text.length();
        HashMap<Character, Integer> frequency = new HashMap<>();

        for(char c : text.toCharArray()) {
            if(frequency.containsKey(c)) {
                frequency.put(c, frequency.get(c) + 1);
            } else {
                frequency.put(c, 1);
            }
        }

        // Shannon's Formula
        double entropy = 0;
        for(char c : frequency.keySet()) {
            double probability = 1.0 * frequency.get(c) / N;
            entropy += (probability * (Math.log(probability) / Math.log(2)));
        }

        entropy = -entropy;

        return entropy;
    }

    public static double chiSquareEnglish(String text) {
        int N = text.length();
        double[] text_frequency = new double[26];
        double[] expected_letter_freq = new double[] {0.082,0.015,0.028,0.043,0.13,0.022,0.02,0.061,0.07,0.0015,0.0077,0.04,0.024,0.067,0.075,0.019,0.00095,0.06,0.063,0.091,0.028,0.0098,0.024,0.0015,0.02,0.00074};

        for(char c : text.toCharArray()) {
            text_frequency[c - 97]++;
        }

        double chi_square = 0;

        for(int i = 0; i < text_frequency.length; i++) {
            chi_square += (Math.pow(text_frequency[i] - (expected_letter_freq[i] * N), 2) / (expected_letter_freq[i] * N));
        }

        return chi_square;
    }

    public static double chiSquareUniform(String text) {
        int N = text.length();
        double[] text_frequency = new double[26];
        for(char c : text.toCharArray()) {
            text_frequency[c - 97]++;
        }

        double chi_square = 0;

        for(int i = 0; i < text_frequency.length; i++) {
            chi_square += (Math.pow(text_frequency[i] - (N / 26.0), 2) / (N / 26.0));
        }

        return chi_square;
    }

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

    public static void saveResults1(ArrayList<String> keys, ArrayList<String> samples, double[] ic, double[] entropy, double[] chi_square, double[] plain, int label, String filename) {
        try {
            FileWriter save = new FileWriter(filename);
            for(int i = 0; i < ic.length; i++) {
                save.write(keys.get(i) + ", " + samples.get(i) + ", " + ic[i] + ", " + entropy[i] + ", " + chi_square[i] +  ", " + plain[i] + ", " + label + "\n");
            }
            save.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveResults2(ArrayList<String> samples, String key, double[] ic, double[] entropy, double[] chi_square, double[] plain, double[] cipher, int label, String filename) {
        try {
            FileWriter save = new FileWriter(filename);
            for(int i = 0; i < plain.length; i++) {
                save.write(key + ", " + samples.get(i) + ", " + ic[i] + ", " + entropy[i] + ", " + chi_square[i] +  ", " + plain[i] + ", " + cipher[i] + ", " + label + "\n");
            }

            save.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testHMM1(int label, String hmmPlainFile, String filename, String saveFile) {
        HiddenMarkovModel english = new HiddenMarkovModel(hmmPlainFile);

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> samples = readFile(filename);

        for(int i = 0; i < samples.size(); i++) {
            String[] tmp = samples.get(i).split(", ");

            keys.add(tmp[0]);
            samples.set(i, tmp[1]);
        }

        double[] plainResults = english.test(samples);

        double[] ic = new double[samples.size()];
        double[] entropy = new double[samples.size()];
        double[] chi_square = new double[samples.size()];

        for(int i = 0; i < samples.size(); i++) {
            ic[i] = indexOfCoincidence(samples.get(i));
            entropy[i] = shannonEntropy(samples.get(i));
            chi_square[i] = chiSquareEnglish(samples.get(i));
        }

        saveResults1(keys, samples, ic, entropy, chi_square, plainResults, label, saveFile);
    }

    /**
     * Testing HMM Function for Experiment 2
     * @param hmmPlainFile: HMM Trained on English Language file
     * @param hmmCipherFile: HMM Trained on Cipher Text file
     * @param filename: Cipher Text Samples file
     */
    public static void testHMM2(String key, int label, String hmmPlainFile, String hmmCipherFile, String filename, String saveFile) {
        HiddenMarkovModel plain = new HiddenMarkovModel(hmmPlainFile);
        HiddenMarkovModel cipher = new HiddenMarkovModel(hmmCipherFile);

        ArrayList<String> samples = readFile(filename);
        double[] plainResults = plain.test(samples);
        double[] cipherResults = cipher.test(samples);

        double[] ic = new double[samples.size()];
        double[] entropy = new double[samples.size()];
        double[] chi_square = new double[samples.size()];


        for(int i = 0; i < samples.size(); i++) {
            ic[i] = indexOfCoincidence(samples.get(i));
            entropy[i] = shannonEntropy(samples.get(i));
            chi_square[i] = chiSquareEnglish(samples.get(i));
        }

        saveResults2(samples, key, ic, entropy, chi_square, plainResults, cipherResults, label, saveFile);
    }

    public static void main(String[] args) {
        //testHMM1(0, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/EnglishHMM.txt",  "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/SimpleSubSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/SimpleSubResults.txt");
        //testHMM1(1, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/EnglishHMM.txt",  "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/ColumnarTransSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/ColumnarTransResults.txt");
        //testHMM1(2, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/EnglishHMM.txt",  "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/VigenereSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/VigenereResults.txt");
        //testHMM1(3, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/EnglishHMM.txt",  "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/PlayFairSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/PlayFairResults.txt");
        //testHMM1(4, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/EnglishHMM.txt",  "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/FourSquareSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment1/FourSquareResults.txt");


        testHMM2("phqgiumeaylnofdxjkrcvstzwb", 0, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/EnglishHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/SimpleSubHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/SimpleSubSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/SimpleSubResults.txt");
        testHMM2("strawberry", 1, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/EnglishHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/ColumnarTransHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/ColumnarTransSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/ColumnarTransResults.txt");
        testHMM2("kryptos", 2, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/EnglishHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/VigenereHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/VigenereSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/VigenereResults.txt");
        testHMM2("pigernoabcdfhklmqstuvwxyz", 3, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/EnglishHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/PlayFairHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/PlayFairSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/PlayFairResults.txt");
        testHMM2("zgptfoihmuwdrcnykeqaxvsbl mfnbdcrhsaxyogvituewlqzkp", 4, "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/EnglishHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/FourSquareHMM.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/FourSquareSamples.txt", "/Users/azsy/Desktop/Jupyter NB/CS185C/Ciphertext-Classification/Dataset/Experiment2/FourSquareResults.txt");

    }
}