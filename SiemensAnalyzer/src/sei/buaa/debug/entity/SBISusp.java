package sei.buaa.debug.entity;

public class SBISusp extends Suspiciousness {

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

}
