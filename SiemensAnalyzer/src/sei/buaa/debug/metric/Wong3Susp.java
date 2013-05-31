package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

public class Wong3Susp implements ISuspsCalculator {


	public double calcSups(StatementSum eSum) {
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		double h = 0;
		
		if (a10 <= 2)h = a10;
		else if (a10 > 2 && a10 <= 10) h = 2 + 0.1*(a10-2);
		else h = 2.8 + 0.001*(a10 - 10);
		
		return a11 - h;
	}
	

}
