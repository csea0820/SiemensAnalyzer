package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class JaccardSusp  implements ISuspsCalculator {

	public double calcSups(StatementSum eSum) {

		int a01 = eSum.getA01();
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		return a11 * 1.0 / (a11 + a01 + a10);
	}


}
