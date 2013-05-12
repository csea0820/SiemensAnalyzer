package sei.buaa.debug.metric;

public class AmpleSusp extends AbstractSuspiciousness {

	public AmpleSusp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		double a = a11*1.0/(a11+a01);
		double b = a10*1.0/(a10+a00);
		this.susp = a-b;
	}
	
	public void calcWeightedSups(double a00, double a01, double a10, double a11) {
		double a = a11*1.0/(a11+a01);
		double b = a10*1.0/(a10+a00);
		this.susp = a-b;
	}

}
