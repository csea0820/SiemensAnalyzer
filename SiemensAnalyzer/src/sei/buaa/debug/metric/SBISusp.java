package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class SBISusp implements ISuspsCalculator {

	public double calcSups(StatementSum eSum) {

		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		double susp = 0.0;
		if (a10 + a11 == 0)
			susp = 0;
		else
			susp = a11 * 1.0 / (a10 + a11);

		return susp;
	}


}
