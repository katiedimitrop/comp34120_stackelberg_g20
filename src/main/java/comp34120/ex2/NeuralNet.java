package comp34120.ex2;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;



public class NeuralNet {
    private static final double DATE_SCALE = 200.0;

    private INDArray leaderPriceFeature;
    private INDArray followerPriceFeature;

    private INDArray dateFeature;


    public NeuralNet(ArrayList<Record> records) {
         int recordsLength = records.size();
         int[] shapeArray = new int[]{recordsLength, 1};
         double[] days = new double[recordsLength];
         double [] lPrices = new double[recordsLength];
         double []  fOutputs = new double[recordsLength];
         int count = 0;

         for(Record r: records){
             lPrices[count] = r.m_leaderPrice;
             fOutputs[count] = r.m_followerPrice;
             days[count] = r.m_date / DATE_SCALE;
             count++;
         }

         this.followerPriceFeature = Nd4j.create(fOutputs, shapeArray);
         this.dateFeature = Nd4j.create(days, shapeArray);
         this.leaderPriceFeature = Nd4j.create(lPrices, shapeArray);
    }

}
