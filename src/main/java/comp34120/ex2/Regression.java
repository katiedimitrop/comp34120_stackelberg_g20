package comp34120.ex2;
import java.util.ArrayList;

public abstract class Regression {

  float a;
  float b;

  ArrayList<Record> records;

  public Regression(ArrayList<Record> records) {
    this.records = records;
  }

  public Regression() {
  }

  abstract void estimateAB();

  public float getFollowerPrice(float leaderPrice) {
    return a + (b * leaderPrice);
  }

  public float getA() { return a; }

  public float getB() { return b; }

  public void setRecords(ArrayList<Record> records) {
    this.records = records;
  }

  @Override
  public String toString() {
    return "Regression{" +
      "a=" + a +
      ", b=" + b +
      '}';
  }
  public void updateRecords(ArrayList<Record> records) { }

}

