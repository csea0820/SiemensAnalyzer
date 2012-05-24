package sei.buaa.debug.entity;

public class JaccardSusp extends Suspiciousness {

	public JaccardSusp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		this.susp = a11*1.0/(a11+a01+a10);
	}

}
