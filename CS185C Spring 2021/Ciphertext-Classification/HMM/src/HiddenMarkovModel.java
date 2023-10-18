import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HiddenMarkovModel {
    int numStates;
    int numSymbols;

    private double[] PI;
    private double[][] A;
    private double[][] B;

    public HiddenMarkovModel(int states, int symbols) {
        this.numStates = states;
        this.numSymbols = symbols;

        this.PI = new double[this.numStates];
        this.A = new double[this.numStates][this.numStates];
        this.B = new double[this.numStates][this.numSymbols];

        initialize();
    }

    public HiddenMarkovModel(String fileload) {
        load(fileload);
    }

    // Load HMM Functions
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

    // Save HMM Functions
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

    // initialize HMM row-stochastic probabilities
    private void initialize() {
        for(int row = 0; row < this.numStates; row++) {
            this.A[row] = rowStochastic(this.numStates);
            this.B[row] = rowStochastic(this.numSymbols);
        }
        this.PI = rowStochastic(this.numStates);
    }

    private double[] rowStochastic(int num) {
        Random rand = new Random();
        double total = 0;
        double[] rowProbabilites = new double[num];

        for(int i = 0; i < num; i++) {
            rowProbabilites[i] = rand.nextInt(10000) + 10000;
            total += rowProbabilites[i];
        }

        for(int j = 0; j < num; j++) {
            rowProbabilites[j] = (rowProbabilites[j] / total);
        }

        return rowProbabilites;
    }

    private double[] matrix1DCopy(double[] matrix) {
        double[] copy = new double[matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i];
        }
        return copy;
    }

    private double[][] matrix2DCopy(double[][] matrix) {
        double[][] copy = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                copy[i][j] = matrix[i][j];
            }
        }
        return copy;
    }

    private double[] forward(char[] observation, double[][] alpha) {
        int T = observation.length;
        double[] c = new double[T]; // Scalars

        // compute alpha[0][i]
        c[0] = 0;
        for(int i = 0; i < this.numStates; i++) {
            alpha[0][i] = this.PI[i] * this.B[i][observation[0] - 97];
            c[0] = c[0] + alpha[0][i];
        }

        // scale alpha[0][i]
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
                alpha[t][i] = alpha[t][i] * this.B[i][observation[t] - 97];
                c[t] = c[t] + alpha[t][i];
            }

            // scale alpha[t][i]
            c[t] = 1.0 / c[t];
            for(int i = 0; i < this.numStates; i++) {
                alpha[t][i] = c[t] * alpha[t][i];
            }
        }

        return c;
    }

    private void backward(char[] observation, double[][] beta, double[] c) {
        int T = observation.length;

        // let beta[T - 1][i] = 1, scaled by c[T - 1]
        for(int i = 0; i < this.numStates; i++) {
            beta[T - 1][i] = c[T - 1];
        }

        // beta pass
        for(int t = T - 2; t >= 0; t--) {
            for(int i = 0; i < this.numStates; i++) {
                beta[t][i] = 0;
                for(int j = 0; j < this.numStates; j++) {
                    beta[t][i] = beta[t][i] + (this.A[i][j] * this.B[j][observation[t + 1] - 97] * beta[t + 1][j]);
                }
                // scale beta[t][i] with same scale factor as alpha[t][i]
                beta[t][i] = c[t] * beta[t][i];
            }
        }
    }

    private void gammaDigamma(char[] observation, double[][] alpha, double[][] beta, double[][] gamma, double[][][] digamma) {
        int T = observation.length;

        for(int t = 0; t < T - 1; t++) {
            double denom = 0;
            for(int i = 0; i < this.numStates; i++) {
                for(int j = 0; j < this.numStates; j++) {
                    denom = denom + (alpha[t][i] * this.A[i][j] * this.B[j][observation[t + 1] - 97] * beta[t + 1][j]);
                }
            }

            for(int i = 0; i < this.numStates; i++) {
                gamma[t][i] = 0;
                for(int j = 0; j < this.numStates; j++) {
                    digamma[t][i][j] = (alpha[t][i] * this.A[i][j] * this.B[j][observation[t + 1] - 97] * beta[t + 1][j]) / denom;
                    gamma[t][i] = gamma[t][i] + digamma[t][i][j];
                }
            }
        }

        // Special case for gamma[T - 1][i]
        double denom = 0;
        for(int i = 0; i < this.numStates; i++) {
            denom = denom + alpha[T - 1][i];
        }

        for(int i = 0; i < this.numStates; i++) {
            gamma[T - 1][i] = alpha[T - 1][i] / denom;
        }
    }

    private void reestimate(char[] observation, double[][] gamma, double[][][] digamma) {
        int T = observation.length;

        // re-estimate PI
        for(int i = 0; i < this.numStates; i++) {
            this.PI[i] = gamma[0][i];
        }

        // re-estimate A
        for(int i = 0; i < this.numStates; i++) {
            double denom = 0;
            for(int t = 0; t < T - 1; t++) {
                denom = denom + gamma[t][i];
            }
            for(int j = 0; j < this.numStates; j++) {
                double numer = 0;
                for(int t = 0; t < T - 1; t++) {
                    numer = numer + digamma[t][i][j];
                }
                this.A[i][j] = numer / denom;
            }
        }

        // re-estimate B
        for(int i = 0; i < this.numStates; i++) {
            double denom = 0;
            for(int t = 0; t < T - 1; t++) {
                denom = denom + gamma[t][i];
            }
            for(int j = 0; j < this.numSymbols; j++) {
                double numer = 0;
                for(int t = 0; t < T; t++) {
                    if(observation[t] - 97 == j) {
                        numer = numer + gamma[t][i];
                    }
                }
                this.B[i][j] = numer / denom;
            }
        }
    }

    private double logProbCalculation(double[] c) {
        double logProb = 0;
        for(int i = 0; i < c.length; i++) {
            logProb = logProb + Math.log10(c[i]);
        }

        return -logProb / c.length;
    }

    private double trainHelper(char[] observation, int maxIterations) {
        double oldLogProb = Double.NEGATIVE_INFINITY;
        int iter = 0;
        boolean flag = true;

        double[][] alpha = new double[observation.length][this.numStates];
        double[][] beta = new double[observation.length][this.numStates];
        double[][] gamma = new double[observation.length][this.numStates];
        double[][][] digamma = new double[observation.length][this.numStates][this.numStates];

        while(flag) {
            double[] c = forward(observation, alpha);
            backward(observation, beta, c);
            gammaDigamma(observation, alpha, beta, gamma, digamma);
            reestimate(observation, gamma, digamma);

            // compute Probability
            double logProb = logProbCalculation(c);

            iter = iter + 1;

            if(iter < maxIterations && logProb > oldLogProb) {
                oldLogProb = logProb;
            } else {
                //System.out.println(iter);
                flag = false;
            }
        }
        return oldLogProb;
    }

    // using random restarts, special case of boosting
    public void train(char[] observation, int maxIterations, int numResets) {
        double oldLogProb = trainHelper(observation, maxIterations);

        System.out.println("Initial LogProb: " + oldLogProb);

        double[] pi = matrix1DCopy(this.PI);
        double[][] transition = matrix2DCopy(this.A);
        double[][] emission = matrix2DCopy(this.B);

        for(int i = 1; i < numResets; i++) {
            System.out.println(i);
            initialize();
            double currentLogProb = trainHelper(observation, maxIterations);

            if(currentLogProb > oldLogProb) {
                oldLogProb = currentLogProb;
                pi = matrix1DCopy(this.PI);
                transition = matrix2DCopy(this.A);
                emission = matrix2DCopy(this.B);
            }
        }

        System.out.println("Best LogProb after " + numResets + " resets: " + oldLogProb);

        this.PI = matrix1DCopy(pi);
        this.A = matrix2DCopy(transition);
        this.B = matrix2DCopy(emission);
    }

    public double[] test(ArrayList<String> sequences) {
        double[] results = new double[sequences.size()];
        int index = 0;

        for(String sequence : sequences) {
            double[][] alpha = new double[sequence.length()][this.numStates];
            double[] c = forward(sequence.toCharArray(), alpha);

            results[index] = logProbCalculation(c);
            index++;
        }
        return results;
    }
}