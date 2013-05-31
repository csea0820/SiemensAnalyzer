package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class WeightedWong3Susp  extends WeightedSusp implements ISuspsCalculator{

	
	public double calcSups(StatementSum eSum) {
		
		calcWeights(eSum.getA00(), eSum.getA01(), eSum.getA10(), eSum.getA11(),
				eSum.getD_a00(),eSum.getD_a01(),eSum.getD_a10(),eSum.getD_a11());
		return calcWeightedSups(d_a00, d_a01, d_a10, d_a11);
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
