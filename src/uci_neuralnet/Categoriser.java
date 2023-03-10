package uci_neuralnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// static frontend class for neural net
public class Categoriser {

    public static final String DATASET_1_FILE_PATH = "./data/dataset_1.csv";
    public static final String DATASET_2_FILE_PATH = "./data/dataset_2.csv";

    public final static boolean SHOW_LABELS = false;

    // the faster the more errors
    public static final double LEARNING_RATE = 0.05; 
    // mess with the randomness of the thing
    public final static double BIAS_RANGE_SMALLEST = -0.5;
    public final static double BIAS_RANGE_BIGGEST = 0.7;
    public final static double WEIGHTS_RANGE_SMALLEST = -1;
    public final static double WEIGHTS_RANGE_BIGGEST = 1;
    
    // increase for accuracy instead performance
    final static int TRAINING_EPOCHS_VALUE = 250;
    final static int TRAINING_LOOPS_VALUE = 500;
    final static int TRAINING_BATCH_SIZE = 32;
    
    final static int FIRST_HIDDEN_LAYER_NODE_AMOUNT = 26;
    final static int SECOND_HIDDEN_LAYER_NODE_AMOUNT = 15;
    // img is 64pixels 8*8
    final static int INPUT_LAYER_NODE_AMOUNT = 64; 
    final static int OUTPUT_DOMAIN = 10;


    // tests neural network using training set
    public static void testModel(NeuralNet net, Dataset set){
		int correct = 0;
        for(int i = 0; i < set.size(); i++){
            double highest = getArrayMax(net.calculateOutput(set.getInput(i)));
            double actualHighest = getArrayMax(set.getOutput(i));
            if(SHOW_LABELS){
                System.out.println("[+] Guess: " + highest + " Real number: " + actualHighest);
            }
            if(highest == actualHighest){
                correct ++;
            }
        }
        System.out.println("[+] Accuracy: " +
        		(double)((double)correct * 100 / (double)set.size()) + "% ");
        	System.out.println(correct + "/" + set.size());
    }


    // reads data file and parses it to memory
    public static Dataset parseDataset(String file_path) throws FileNotFoundException{
        Dataset set = new Dataset(INPUT_LAYER_NODE_AMOUNT, 10);
        Scanner scanner = new Scanner(new File(file_path));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            int lastCommaIndex = line.lastIndexOf(',');
            int label = Integer.parseInt(line.substring(lastCommaIndex+1, lastCommaIndex +2));
            String newLine = line.substring(0,lastCommaIndex);
            String[] splitLine = newLine.split(",");

            double[] splitLineNumber = new double[splitLine.length];
            for(int i = 0; i<splitLine.length; i++){
                splitLineNumber[i] = Double.parseDouble(splitLine[i]);
            }
            double[] output = new double[10];
            output[label] = 1d;
            set.addData(splitLineNumber, output);
        }
        scanner.close();
        return set;
    }


    // trains neural net for a specific metric of time and bias
    // takes neural net, training data (parsed)
    // bias n of epochs for training and m iterations per epoch
    // flag large model improve performance and maybe accuracy
    public static void trainModel(NeuralNet net, Dataset set, int epochs, int loops, int batch_size){
        System.out.println("[*] Training neural network...");
        for(int epoch= 0; epoch < epochs; epoch++){
            net.train(set, loops, batch_size);
            if(SHOW_LABELS){
                System.out.println("Epochs : " + epoch + "/" + epochs);
            }
        }
    }
    
    // get index of highest value in array
    public static int getArrayMax(double[] input){
        int ret = 0;
        for(int iterationIndex = 1; iterationIndex < input.length; iterationIndex++){
            if(input[iterationIndex] > input[ret]){
            	ret = iterationIndex;
            }
        }
        return ret;
    }

}
