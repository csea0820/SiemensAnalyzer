package sei.buaa.debug.entity;

public class TarantulaSusp extends Suspiciousness {

	public TarantulaSusp(int ln) {
		super(ln);
	}
	
	public void calcSups(int a00,int a01,int a10,int a11)
	{
		double res = (a10*1.0/a11)*((a11+a01)*1.0/(a10+a00));
		this.susp = 1.0/(1+res);
	}

}
