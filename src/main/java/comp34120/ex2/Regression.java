package comp34120.ex2;
import java.util.ArrayList;

public abstract class Regression {

  float a;
  float b;

  ArrayList<Record> records;

  public Regression(ArrayList<Record> records) {
    this.records = records;
  }

  abstract void estimateAB();

  public float getFollowerPrice(float leaderPrice) {
    return a + (b * leaderPrice);
  }

  public float getA() {
    return a;
  }

  public float getB() {
    return b;
  }

  @Override
  public String toString() {
    return "Regression{" +
      "a=" + a +
      ", b=" + b +
      '}';
  }
}
