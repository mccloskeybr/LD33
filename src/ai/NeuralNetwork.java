package ai;

/**
 * Single hidden_1 layer neural network
 */
public class NeuralNetwork {

    public static final int N_INPUT = 2;
    public static final int N_HIDDEN = 5;
    public static final int N_OUTPUT = 4;

    private double[] weights;
    private double[] hidden_1;
    private double[] hidden_2;
    private double[] output;

    public NeuralNetwork(){

        weights = new double[N_INPUT * N_HIDDEN + N_HIDDEN * N_HIDDEN + N_HIDDEN * N_OUTPUT];

        //populate the weights with random doubles (-1, 1)
        for (int i = 0; i < weights.length; i++)
            weights[i] = Math.random() - Math.random();

        hidden_1 = new double[N_HIDDEN];
        hidden_2 = new double[N_HIDDEN];
        output = new double[N_OUTPUT];

    }

    public NeuralNetwork(double[] weights) {

        this.weights = weights;
        hidden_1 = new double[N_HIDDEN];
        hidden_2 = new double[N_HIDDEN];
        output = new double[N_OUTPUT];

    }

    public double[] getWeights(){
        return weights;
    }

    public double[] forward(double[] input){

        if (input.length == N_INPUT) {

            // input to hidden_1
            for (int i = 0; i < input.length; i++)
                for (int j = 0; j < hidden_1.length; j++)
                    hidden_1[j] += input[i] * weights[i * N_HIDDEN + j];

            // activate hidden_1
            for (int i = 0; i < hidden_1.length; i++)
                hidden_1[i] = sigmoid(hidden_1[i]);

            // hidden_1 to hidden_2
            for (int i = 0; i < hidden_1.length; i++)
                for (int j = 0; j < hidden_2.length; j++)
                    hidden_2[j] += hidden_1[i] * weights[(N_INPUT * N_HIDDEN) + i * N_HIDDEN + j];

            // activate hidden_2
            for (int i = 0; i < hidden_2.length; i++)
                hidden_2[i] = sigmoid(hidden_2[i]);

            // hidden_2 to output
            for (int i = 0; i < hidden_2.length; i++)
                for (int j = 0; j < output.length; j++)
                    output[j] += hidden_2[i] * weights[(N_INPUT * N_HIDDEN) + (N_HIDDEN * N_HIDDEN) + i * N_OUTPUT + j];

            // activate output
            for (int i = 0; i < output.length; i++)
                output[i] = sigmoid(output[i]);

            return output;

        }

        return null;
    }

    private double sigmoid(double x){

        return 1 / (1 + Math.pow(Math.E, -4.9 * x));

    }

}
