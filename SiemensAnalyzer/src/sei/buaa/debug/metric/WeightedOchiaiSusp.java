package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class WeightedOchiaiSusp extends WeightedSusp implements ISuspsCalculator{


	public double calcSups(StatementSum eSum) {
		calcWeights(eSum.getA00(), eSum.getA01(), eSum.getA10(), eSum.getA11(),
				eSum.getD_a00(), eSum.getD_a01(), eSum.getD_a10(),
				eSum.getD_a11());
		return calcWeightedSups(d_a00, d_a01, d_a10, d_a11);
	}
	
	public double calcWeightedSups(double a00, double a01, double a10,
			double a11) {
		double susp = 0.0;
		if (a10+a11 == 0.0)susp = 0;
		else
		{
			double res = Math.sqrt((a11+a01)*(a11+a10)*1.0);
			susp = a11/res;
		}
		
		return susp;
	}

}
