package sei.buaa.debug.metric;


public class SIQSusp extends AbstractSuspiciousness {

	public SIQSusp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		double If = -Math.log((a01+a11)*1.0/(a00+a01+a10+a11));
		double Ip = -Math.log((a00+a10)*1.0/(a00+a01+a10+a11));
		
		double Ies = caclExecutedSIQ(a00, a01, a10, a11);
		double Ins = caclNotExecutedSIQ(a00, a01, a10, a11);
		
		this.susp = (Ies*If*a11+Ins*Ip*a00)/(Ies*Ip*a10+Ins*If*a01);
	}
	
	private double caclExecutedSIQ(int a00, int a01, int a10, int a11)
	{
		if (a10+a11 == 0)
			return -Math.log(1.0/(a00+a01+a10+a11));
		
		if (a00+a01 == 0)
			return -Math.log((1.0-1.0/(a00+a01+a10+a11)));
		
		return -Math.log((a11+a10)*1.0/(a00+a01+a10+a11));
	}
	
	private double caclNotExecutedSIQ(int a00, int a01, int a10, int a11)
	{
		if (a00+a01 == 0)
			return -Math.log(1.0/(a00+a01+a10+a11));
		
		if (a10+a11 == 0)
			return -Math.log((1.0-1.0/(a00+a01+a10+a11)));
		
		return -Math.log((a01+a00)*1.0/(a00+a01+a10+a11));
	}
	

}
