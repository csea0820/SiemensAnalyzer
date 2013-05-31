package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class WeightedOpSusp extends WeightedSusp implements ISuspsCalculator{


	public double calcSups(StatementSum eSum) {
		calcWeights(eSum.getA00(), eSum.getA01(), eSum.getA10(), eSum.getA11(),
				eSum.getD_a00(), eSum.getD_a01(), eSum.getD_a10(),
				eSum.getD_a11());
		return calcWeightedSups(d_a00, d_a01, d_a10, d_a11);
	}
	
	public double calcWeightedSups(double a00, double a01, double a10,
			double a11) {
		double b = a10*1.0/(a10+a00+1);
		return a11-b;
	}

}
