package sei.buaa.debug.metric;


public class SBISusp extends AbstractSuspiciousness {

	public SBISusp(int ln) {
		super(ln);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		if (a10+a11 == 0)this.susp = 0;
		else
		this.susp = a11*1.0/(a10+a11);
	}
	
	public void calcWeightedSups(double a00, double a01, double a10, double a11) {
		if (a10+a11 == 0)this.susp = 0;
		else
		this.susp = a11*1.0/(a10+a11);
	}

}
