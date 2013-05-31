package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class LeeTarantulaSusp extends WeightedSusp implements ISuspsCalculator{

	double totalWeigth;
	
	public LeeTarantulaSusp(double totalWeight) {
		this.totalWeigth = totalWeight;
	}
	
	public double calcSups(StatementSum eSum) 
	{
		calcLeeWeights(eSum.getA00(),eSum.getA01(),eSum.getA10(),eSum.getA11(),eSum.getD_aef(),totalWeigth);
		return calcWeightedSups(l_a00, l_a01, l_a10, l_a11);
	}
	
	public double calcWeightedSups(double a00, double a01, double a10,
			double a11) {
		double susp = 0;
		double a = a11 * 1.0 / (a11 + a01);
		double b = a10 * 1.0 / (a10 + a00);
		if (a + b == 0)
			susp = 0;
		else
			susp = a / (a + b);
		return susp;
	}

}
