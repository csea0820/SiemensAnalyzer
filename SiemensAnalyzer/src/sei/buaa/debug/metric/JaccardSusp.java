package sei.buaa.debug.metric;


public class JaccardSusp extends AbstractSuspiciousness {

	public JaccardSusp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		this.susp = a11*1.0/(a11+a01+a10);
	}
	
	public void calcWeightedSups(double a00, double a01, double a10, double a11) {
		this.susp = a11*1.0/(a11+a01+a10);
	}

}
