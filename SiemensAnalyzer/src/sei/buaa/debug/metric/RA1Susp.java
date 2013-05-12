package sei.buaa.debug.metric;

public class RA1Susp extends AbstractSuspiciousness {

	public RA1Susp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		
		double v = 0.0;
		if (a01+a11 != 0)v = (a00+a10)*1.0/(a01+a11);
		
		this.susp = (a11+1)*a11*1.0/2*v-a10;
	}
	
	public void calcWeightedSups(double a00, double a01, double a10, double a11) {
		
		double v = 0.0;
		if (a01+a11 != 0)v = (a00+a10)*1.0/(a01+a11);
		
		this.susp = (a11+1)*a11*1.0/2*v-a10;
	}

}
