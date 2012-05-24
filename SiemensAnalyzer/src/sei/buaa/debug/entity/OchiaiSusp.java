package sei.buaa.debug.entity;

public class OchiaiSusp extends Suspiciousness {

	public OchiaiSusp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		double res = Math.sqrt((a11+a01)*(a11+a10)*1.0);
		this.susp = a11/res;
	}

}
