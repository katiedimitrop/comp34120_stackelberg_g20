package comp34120.ex2;

public class StandardMaximiser implements Maximiser{
    float UNIT_COST = 1.00f;
    private float priceUpperLimit = 15.0f;
    private float priceStep = 0.005f;

    public float getProfit(int day, float lPrice, float fPrice){
        float demandModel = 2.00f - lPrice + (0.3f * fPrice);
        return (lPrice - UNIT_COST) * demandModel;
    }

    public float getBestPrice(Regression rModel, int day){
        float bestPrice = 1.0f;
        float bestProfit = -99999.00f;
        for(float price = 1.0f; price <= priceUpperLimit; price += priceStep){
            float estimateFPrice = rModel.getFollowerPrice(price);
            float estimateProfit = getProfit(day, price, estimateFPrice);
            if(estimateProfit > bestProfit){
                bestProfit = estimateProfit;
                bestPrice = price;
            }
        }
        return bestPrice;
    }

}



