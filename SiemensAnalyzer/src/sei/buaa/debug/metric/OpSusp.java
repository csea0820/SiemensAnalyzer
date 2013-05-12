package sei.buaa.debug.metric;

public class OpSusp extends AbstractSuspiciousness {

	public OpSusp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		double b = a10*1.0/(a10+a00+1);
		this.susp = a11-b;
	}
	
	public void calcWeightedSups(double a00, double a01, double a10, double a11) {
		double b = a10*1.0/(a10+a00+1);
		this.susp = a11-b;
	}

}