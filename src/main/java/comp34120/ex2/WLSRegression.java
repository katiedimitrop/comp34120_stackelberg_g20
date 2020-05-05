package comp34120.ex2;
import java.util.ArrayList;

public class WLSRegression extends Regression {

  final double FORGETTING_FACTOR = 0.95;

  public WLSRegression(ArrayList<Record> records) {
    super(records);
  }

  @Override
  public void estimateAB() {

    float sumX = 0;
    float sumXSquared = 0;
    float sumY = 0;
    float sumXY = 0;

    for(int i = 0; i < records.size(); i++) {
      Record record = records.get(i);
      double forgetting = Math.pow(FORGETTING_FACTOR, records.size() - i);
      sumX += forgetting * record.m_leaderPrice;
      sumY += forgetting * record.m_followerPrice;
      sumXSquared += forgetting * record.m_leaderPrice * record.m_leaderPrice;
      sumXY += forgetting * record.m_leaderPrice * record.m_followerPrice;
    }

    a = ((sumXSquared * sumY) - (sumX * sumXY)) / ((records.size() * sumXSquared) - (sumX * sumX));
    b = ((records.size() * sumXY) - (sumX * sumY)) / ((records.size() * sumXSquared) - (sumX * sumX));
  }
}
