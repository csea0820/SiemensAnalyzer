package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class LeeJaccardSusp  extends WeightedSusp implements ISuspsCalculator {

	double totalWeight;
	
	public LeeJaccardSusp(double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public double calcSups(StatementSum eSum) 
	{
		calcLeeWeights(eSum.getA00(),eSum.getA01(),eSum.getA10(),eSum.getA11(),eSum.getD_aef(),totalWeight);
		return calcWeightedSups(l_a00, l_a01, l_a10, l_a11);
	}
	
	public double calcWeightedSups(double a00, double a01, double a10,
			double a11) {
		return a11 * 1.0 / (a11 + a01 + a10);
	}

}
