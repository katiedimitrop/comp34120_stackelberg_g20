package g20Bot;

public interface Maximiser {
    public float getProfit(int day, float lPrice, float fPrice);

    public float getBestPrice(Regression rModel, int day);
}