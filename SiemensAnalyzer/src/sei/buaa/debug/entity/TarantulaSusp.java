package sei.buaa.debug.entity;

public class TarantulaSusp extends Suspiciousness {

	public TarantulaSusp(int ln) {
		super(ln);
	}
	
	/*
	 * a00:test case that don't execute the statement passes
	 * a01:test case that don't execute the statement fails
	 * a10:test case that execute the statement passes
	 * a11:test case that execute the statement fails
	 */
	public void calcSups(int a00,int a01,int a10,int a11)
	{
		double a = a11*1.0/(a11+a01);
		double b = a10*1.0/(a10+a00);
		if (a+b == 0)this.susp = 0;
		else 
		this.susp = a/(a+b);
	}

}
