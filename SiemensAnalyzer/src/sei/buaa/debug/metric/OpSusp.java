package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class OpSusp implements ISuspsCalculator{




	public double calcSups(StatementSum eSum) {
		int a00 = eSum.getA00();
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		
		double b = a10*1.0/(a10+a00+1);
		return a11-b;
	}
	

}
