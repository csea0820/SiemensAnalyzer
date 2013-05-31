package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;


public class OchiaiSusp  implements ISuspsCalculator {




	public double calcSups(StatementSum eSum) {
		int a01 = eSum.getA01();
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		double susp = 0.0;
		if (a10+a11 == 0)susp = 0;
		else
		{
			double res = Math.sqrt((a11+a01)*(a11+a10)*1.0);
			susp = a11/res;
		}
		
		return susp;
	}

}
