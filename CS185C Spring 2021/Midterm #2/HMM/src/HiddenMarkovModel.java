/*
 * HMM Notation:
 *      T = Length of the observation sequence
 *      N = Number of states in the model
 *      M = Number of observations symbols
 *      Q = Distinct states of the Markov Process
 *      V = Possible observations
 *      A = State transitions probabilities
 *      B = Observation probability matrix
 *      PI = Initial state distribution
 *      O = Observation sequence, O(0), O(1), O(2), ..., O(T - 1)
 */
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.stream.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HiddenMarkovModel {
    private int numStates;
    private int numSymbols;

    private double[] PI;
    private double[][] A;
    private double[][] B;

    public HiddenMarkovModel(int states, int symbols) {
        this.numSymbols = symbols;
        this.numStates = states;

        this.PI = new double[this.numStates];
        this.A = new double[this.numStates][this.numStates];
        this.B = new double[this.numStates][this.numSymbols];

        initialize();
    }

    public HiddenMarkovModel(String fileload) {
        load(fileload);
    }

    // initialize HMM probabilities
    private void initialize() {
        for(int i = 0; i < this.numStates; i++) {
            this.A[i] = calcProbabilities(this.numStates);
            this.B[i] = calcProbabilities(this.numSymbols);
        }

        this.PI = calcProbabilities(this.numStates);
    }

    private double[] calcProbabilities(int num) {
        Random rand = new Random();
        double[] randProbs = new double[num];
        double total = 0;

        for(int i = 0; i < num; i++) {
            randProbs[i] = rand.nextInt(10000) + 10000;
            total += randProbs[i];
        }

        for(int j = 0; j < num; j++) {
            randProbs[j] = (randProbs[j] / total);
        }

        return randProbs;
    }

    // Load HMM functions
    private double[] stringToArray(String string) {
        return Stream.of(string.split(",")).mapToDouble(Double :: parseDouble).toArray();
    }

    public void load(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));

            this.PI = stringToArray(sc.nextLine());

            sc.nextLine();

            ArrayList<double[]> transition = new ArrayList<>();
            ArrayList<double[]> emission = new ArrayList<>();

            String line;

            while(!(line = sc.nextLine()).equals("")) {
                transition.add(stringToArray(line));
            }

            while(sc.hasNext() && !(line = sc.nextLine()).equals("")) {
                emission.add(stringToArray(line));
            }

            this.A = transition.stream().toArray(double[][] :: new);
            this.B = emission.stream().toArray(double[][] :: new);

            this.numStates = this.A.length;
            this.numSymbols = this.B[0].length;

            sc.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Save HMM functions
    private String arrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]);
            sb.append(',');
        }

        sb.append(array[array.length - 1]);
        sb.append('\n');

        return sb.toString();
    }

    public void save(String filename) {
        try {
            FileWriter saving = new FileWriter(filename);
            saving.write(arrayToString(this.PI));
            saving.write('\n');

            for(double[] arr : this.A) {
                saving.write(arrayToString(arr));
            }
            saving.write('\n');

            for(double[] arr : this.B) {
                saving.write(arrayToString(arr));
            }

            saving.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // calculations for HMM
    private double[] forward(char[] observation, double[][] alpha) {
        int T = observation.length;
        double[] c = new double[T];

        // compute alpha[0][i]
        c[0] = 0;
        for(int i = 0; i < this.numStates; i++) {
            alpha[0][i] = this.PI[i] * this.B[i][observation[0] - 65];
            c[0] = c[0] + alpha[0][i];
        }

        // scale the alpha[0][i]
        c[0] = 1.0 / c[0];
        for(int i = 0; i < this.numStates; i++) {
            alpha[0][i] = c[0] * alpha[0][i];
        }

        // compute alpha[t][i]
        for(int t = 1; t < T; t++) {
            c[t] = 0;
            for(int i = 0; i < this.numStates; i++) {
                alpha[t][i] = 0;
                for(int j = 0; j < this.numStates; j++) {
                    alpha[t][i] = alpha[t][i] + (alpha[t - 1][j] * this.A[j][i]);
                }
                alpha[t][i] *= this.B[i][observation[t] - 65];
                c[t] += alpha[t][i];
            }

            // scale alpha[t][i]
            c[t] = 1 / c[t];
            for(int i = 0; i < this.numStates; i++) {
                alpha[t][i] *= c[t];
            }
        }
        return c;
    }

    private void backward(char[] observation, double[][] beta, double[] c) {
        int T = observation.length;

        for(int i = 0; i < this.numStates; i++) {
            beta[T - 1][i] = c[T - 1];
        }

        // beta-pass
        for(int t = T - 2; t >= 0; t--) {
            for(int i = 0; i < this.numStates; i++) {
                beta[t][i] = 0;
                for(int j = 0; j < this.numStates; j++) {
                    beta[t][i] = beta[t][i] + (this.A[i][j] * this.B[j][observation[t + 1] - 65] * beta[t + 1][j]);
                }
                beta[t][i] *= c[t];
            }
        }
    }

    private void gammas(char[] observation, double[][] alpha, double[][] beta, double[][] gamma, double[][][] digamma) {
        int T = observation.length;

        for(int t = 0; t < T - 1; t++) {
            double denom = 0;
            for(int i = 0; i < this.numStates; i++) {
                for(int j = 0; j < this.numStates; j++) {
                    denom += alpha[t][i] * this.A[i][j] * this.B[j][observation[t + 1] - 65] * beta[t + 1][j];
                }
            }

            for(int i = 0; i < this.numStates; i++) {
                gamma[t][i] = 0;
                for(int j = 0; j < this.numStates; j++) {
                    digamma[t][i][j] = (alpha[t][i] * this.A[i][j] * this.B[j][observation[t + 1] - 65] * beta[t + 1][j]) / denom;
                    gamma[t][i] += digamma[t][i][j];
                }
            }
        }

        // special case
        double denom = 0;
        for(int i = 0; i < this.numStates; i++) {
            denom += alpha[T - 1][i];
        }

        for(int i = 0; i < this.numStates; i++) {
            gamma[T - 1][i] = alpha[T - 1][i] / denom;
        }
    }

    private void reestimate(char[] observation, double[] pi_c, double[][] gamma, double[][][] digamma, double[][] a_denom, double [][] a_numer, double[][] b_denom, double[][] b_numer) {
        int T = observation.length;

        // re-estimate PI
        for(int i = 0; i < this.numStates; i++) {
            pi_c[i] += gamma[0][i];
        }

        // re-estimate A
        for(int i = 0; i < this.numStates; i++) {
            for(int j = 0; j < this.numStates; j++) {
                for(int t = 0; t < T - 1; t++) {
                    a_numer[i][j] += digamma[t][i][j];
                    a_denom[i][j] += gamma[t][i];
                }
            }
        }

        // re-estimate B
        for(int i = 0; i < this.numStates; i++) {
            for(int j = 0; j < this.numSymbols; j++) {
                for(int t = 0; t < T - 1; t++) {
                    if(observation[t] - 65 == j) {
                        b_numer[i][j] += gamma[t][i];
                    }
                    b_denom[i][j] += gamma[t][i];
                }
            }
        }
    }

    private double logProb(double[] c) {
        double logProb = 0;
        for(int t = 0; t < c.length; t++) {
            logProb += Math.log10(c[t]);
        }
        return -logProb / c.length;
    }
    public void train(ArrayList<String> observations) {
        int maxIterations =  10;
        int iterations = 0;
        double oldLogProb = Double.NEGATIVE_INFINITY;
        int K = observations.size();

        boolean flag = true;

        while(flag) {
            double[][] a_numer= new double[this.numStates][this.numStates];
            double[][] a_denom = new double[this.numStates][this.numStates];
            double[][] b_numer = new double[this.numStates][this.numSymbols];
            double[][] b_denom = new double[this.numStates][this.numSymbols];
            double[] pi_c = new double[this.numStates];

            double logProbSum = 0;

            for(String observation : observations) {
                double[][] alpha = new double[observation.length()][numStates];
                double[][] beta = new double[observation.length()][numStates];
                double[][] gamma = new double[observation.length()][numStates];
                double[][][] digamma = new double[observation.length()][numStates][numStates];

                double[] c = forward(observation.toCharArray(), alpha);
                backward(observation.toCharArray(), beta, c);
                gammas(observation.toCharArray(), alpha, beta, gamma, digamma);
                reestimate(observation.toCharArray(), pi_c, gamma, digamma, a_denom, a_numer, b_denom, b_numer);
                logProbSum += logProb(c);
            }

            // re-estimate PI, A and B
            for(int j = 0; j < this.numStates; j++) {
                this.PI[j] = pi_c[j] / K;

                for(int k = 0; k < this.numStates; k++) {
                    this.A[j][k] = a_numer[j][k] / a_denom[j][k];
                }
                for(int k = 0; k < this.numSymbols; k++) {
                    this.B[j][k] = b_numer[j][k] / b_denom[j][k];
                }
            }

            iterations += 1;

            if(iterations < maxIterations && logProbSum > oldLogProb) {
                oldLogProb = logProbSum;
                System.out.println("Iteration #" + iterations + " completed...");
            } else {
                flag = false;
                System.out.println("Finished");
            }
        }
    }

    public double[] test(ArrayList<String> sequences) {
        double[] results = new double[sequences.size()];
        int index = 0;
        for(String sequence : sequences) {
            double[][] alpha = new double[sequence.length()][this.numStates];
            double[] c = forward(sequence.toCharArray(), alpha);

            results[index] = logProb(c);
            index++;
        }
        return results;
    }

    public int getNumStates() {
        return this.numStates;
    }

    public int getNumSymbols() {
        return this.numSymbols;
    }

    public double[] getPI() {
        return this.PI;
    }

    public double[][] getA() {
        return this.A;
    }

    public double[][] getB() {
        return this.B;
    }
}