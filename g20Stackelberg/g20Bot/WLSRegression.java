package g20Bot;
import comp34120.ex2.Record;
import java.util.ArrayList;

public class WLSRegression extends Regression {

  public WLSRegression(ArrayList<Record> records) {
    super(records);
  }

  @Override
  public void estimateAB() {

    float sumX = 0;
    float sumXSquared = 0;
    float sumY = 0;
    float sumXY = 0;

    for(Record record: records) {
      sumX += record.m_leaderPrice;
      sumY += record.m_followerPrice;
      sumXSquared += record.m_leaderPrice * record.m_leaderPrice;
      sumXY += record.m_leaderPrice * record.m_followerPrice;
    }

    a = ((sumXSquared * sumY) - (sumX * sumXY)) / ((records.size() * sumXSquared) - (sumX * sumX));
    b = ((records.size() * sumXY) - (sumX * sumY)) / ((records.size() * sumXSquared) - (sumX * sumX));
  }
}
