/**
 * csea 2012-12-4 下午1:04:05
 */
package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;

/**
 * @author csea
 *
 */
public class Wong2Susp implements ISuspsCalculator{

	public double calcSups(StatementSum eSum) {
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		return (a11-a10)*1.0;
	}
	
}
