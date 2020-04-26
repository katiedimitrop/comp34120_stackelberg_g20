package comp34120.ex2;
/**
 * Interface for Maximiser Functions
 * @author Ska
 */
public interface Maximiser {

    public float getProfit(int day, float lPrice, float fPrice);

    public float getBestPrice(Regression rModel, int day);

}