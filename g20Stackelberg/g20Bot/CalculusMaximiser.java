package g20Bot;

public class CalculusMaximiser implements Maximiser{

    float UNIT_COST = 1.00f;

    public CalculusMaximiser(){
    }

    public float getProfit(int day, float lPrice, float fPrice){
        float demandModel = 2.00f - lPrice + (0.3f * fPrice);
        return (lPrice - UNIT_COST) * demandModel;
    }

    //LP = Leader Price UC = Unit Cost (1.00) FP = Follower Price
    //FP = a + bLP
    //Profit = (LP - UC) * (2 - LP + 0.3FP)
    //Profit = (LP - 1.00) * (2 - LP + 0.3(a + bLP))
    //J(LP) = (LP - 1.00) * (2 - LP + 0.3(a + bLP))
    //LP is Max when J'(LP) = 0
    //There we can work out maximum using the following formula
    //LP = (-0.3a + 0.3b - 3) / (0.6 - 2)
    @Override
    public float getBestPrice(Regression rModel, int day){
        float a = rModel.getA();
        float b = rModel.getB();
        return ((0.3f*b) - (0.3f*a) - 3.00f)/ ((0.6f*b) - 2.00f);
    }
}