/**
 * csea 2012-12-4 下午1:04:05
 */
package sei.buaa.debug.metric;

/**
 * @author csea
 *
 */
public class Wong2Susp extends AbstractSuspiciousness {

	public Wong2Susp(int ln) {
		super(ln);
	}

	/* (non-Javadoc)
	 * @see sei.buaa.debug.metric.AbstractSuspiciousness#calcSups(int, int, int, int)
	 */
	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		this.susp = a11-a10;
	}

}
