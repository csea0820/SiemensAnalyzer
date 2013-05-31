package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class RA1Susp implements ISuspsCalculator {


	public double calcSups(StatementSum eSum) {
		int a00 = eSum.getA00();
		int a01 = eSum.getA01();
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		
		double v = 0.0;
		if (a01+a11 != 0)v = (a00+a10)*1.0/(a01+a11);
		
		return (a11+1)*a11*1.0/2*v-a10;
	}
	


}
