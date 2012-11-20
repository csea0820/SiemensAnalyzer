package sei.buaa.debug.metric;


public class TarantulaSusp extends AbstractSuspiciousness {

	public TarantulaSusp(int ln) {
		super(ln);
	}
	
	
	public void calcSups(int a00,int a01,int a10,int a11)
	{
		double a = a11*1.0/(a11+a01);
		double b = a10*1.0/(a10+a00);
		if (a+b == 0)this.susp = 0;
		else 
		this.susp = a/(a+b);
	}

}
