package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class LeeWong3Susp extends WeightedSusp implements ISuspsCalculator {

	double totalWeight;

	public LeeWong3Susp(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public double calcSups(StatementSum eSum) {
		calcLeeWeights(eSum.getA00(), eSum.getA01(), eSum.getA10(),
				eSum.getA11(), eSum.getD_aef(), totalWeight);
		return calcWeightedSups(l_a00, l_a01, l_a10, l_a11);
	}

	public double calcWeightedSups(double a00, double a01, double a10,
			double a11) {
		double h = 0;
		
		if (a10 <= 2)h = a10;
		else if (a10 > 2 && a10 <= 10) h = 2 + 0.1*(a10-2);
		else h = 2.8 + 0.001*(a10 - 10);
		
		return a11 - h;
	}


}
