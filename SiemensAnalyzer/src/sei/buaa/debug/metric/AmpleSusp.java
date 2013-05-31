package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class AmpleSusp implements ISuspsCalculator{

	public double calcSups(StatementSum eSum) {

		int a00 = eSum.getA00();
		int a01 = eSum.getA01();
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		double a = a11*1.0/(a11+a01);
		double b = a10*1.0/(a10+a00);
		return a-b;
	}
	

}
