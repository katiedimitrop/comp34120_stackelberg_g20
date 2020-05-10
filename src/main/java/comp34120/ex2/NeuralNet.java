package comp34120.ex2;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.util.ArrayList;
import comp34120.ex2.Record;
import org.nd4j.linalg.dataset.DataSet;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
//Assume follower's reaction function is of the form
//UF = R(UL)= a + bUL
public class NeuralNet extends Regression  {
    private static final double DATE_SCALE = 200.0;

    private INDArray leaderPriceFeature;
    private INDArray followerPriceFeature;

    private INDArray dateFeature;
    private INDArray inputData;
    private INDArray constFeature;
    private DataSet allData;
    int rngSeed = 123; // random number seed for reproducibility
    private static final int nEpochs = 51;

    private static final int numInput = 1;
    private static final int numOutputs = 1;

    //initialize the neural network object and architecture
    private final MultiLayerNetwork neuralNetwork;

    //This function sets the records
    public NeuralNet(ArrayList<Record> records) throws Exception {
        super(records);
        int recordsLength = records.size();
        int[] shapeArray = new int[]{recordsLength, 1};
        double[] days = new double[recordsLength];
        double[] lPrices = new double[recordsLength];
        double[] fOutputs = new double[recordsLength];
        double[] constFeatureArr = new double[recordsLength];
        int count = 0;
        for (Record r : records) {
            lPrices[count] = r.m_leaderPrice;
            fOutputs[count] = r.m_followerPrice;
            days[count] = r.m_date / DATE_SCALE;
            constFeatureArr[count] = 1;
            count++;
        }

        //Create ND4j arrays
        this.followerPriceFeature = Nd4j.create(fOutputs, shapeArray);
        this.dateFeature = Nd4j.create(days, shapeArray);
        this.leaderPriceFeature = Nd4j.create(lPrices, shapeArray);

        //We need to normalize our data. We'll use NormalizeStandardize
        // (which gives us mean 0, unit variance):
        this.allData = new DataSet(leaderPriceFeature, followerPriceFeature);

        this.neuralNetwork = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(rngSeed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.ZERO)
                //removing this worsens performance significantly
                .updater(new Nesterovs(0.9))
                .list()
                .layer(0, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(numInput).nOut(numOutputs).build())
                .build());
        neuralNetwork.init();
    }//constructor

    //This function trains the neural network on the current record and sets a and b based on the
    //result
    @Override
    public void estimateAB() {

        DataSetIterator allDataIterator = new ListDataSetIterator<>(allData.asList());

        System.out.println("Training neural Network....");
        //Training the NN
        for (int i = 0; i < nEpochs; i++) {
            allDataIterator.reset();
            neuralNetwork.fit(allDataIterator);
        }

        Map<String, INDArray> paramTable = neuralNetwork.paramTable();
        Set<String> keys = paramTable.keySet();
        Iterator<String> it = keys.iterator();
        float[] weights_and_biases = new float[2];
        //Prints out the weights learned by the neural network
        int index = 0;
        while (it.hasNext()) {
            String key = it.next();
            INDArray values = paramTable.get(key);
            //System.out.print(key+" ");//print keys
            //System.out.println(Arrays.toString(values.shape()));//print shape of INDArray

            weights_and_biases[index] = values.getFloat(0);
            index = index +1;
        }
        //The weight is stored first
        b = weights_and_biases[0];
        //then the bias
        a = weights_and_biases[1];
        //System.out.println("Linear NN a:" +a);
        //System.out.println("Linear NN b:" +b);
        //System.out.println("R(X) = " +b+"X"+" + "+a);

        System.out.println("****************Simulation day finished********************");
    }

    @Override
    public void updateRecords(ArrayList<Record> records) {
        System.out.println("Updating NN records: ");
        //temporary variables
        int recordsLength = records.size();
        int[] shapeArray = new int[]{recordsLength, 1};
        double[] days = new double[recordsLength];
        double[] lPrices = new double[recordsLength];
        double[] fOutputs = new double[recordsLength];

        int count = 0;
        for (Record r : records)
        {
            //System.out.println("day"+r.m_date);
            lPrices[count] = r.m_leaderPrice;
            fOutputs[count] = r.m_followerPrice;
            days[count] = r.m_date / DATE_SCALE;
            count++;
        }

        //Convert temporary array representations to nd4j arrays
        this.followerPriceFeature = Nd4j.create(fOutputs, shapeArray);
        this.dateFeature = Nd4j.create(days, shapeArray);
        this.leaderPriceFeature = Nd4j.create(lPrices, shapeArray);
        this.allData = new DataSet(leaderPriceFeature, followerPriceFeature);

    }
}//class




