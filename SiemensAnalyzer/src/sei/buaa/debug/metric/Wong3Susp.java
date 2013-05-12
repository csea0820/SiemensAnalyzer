package sei.buaa.debug.metric;

public class Wong3Susp extends AbstractSuspiciousness {

	public Wong3Susp(int ln) {
		super(ln);
	}

	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {
		double h = 0;
		
		if (a10 <= 2)h = a10;
		else if (a10 > 2 && a10 <= 10) h = 2 + 0.1*(a10-2);
		else h = 2.8 + 0.001*(a10 - 10);
		
		this.susp = a11 - h;
	}
	
	public void calcWeightedSups(double a00, double a01, double a10, double a11) {
		double h = 0;
		
		if (a10 <= 2)h = a10;
		else if (a10 > 2 && a10 <= 10) h = 2 + 0.1*(a10 - 2);
		else h = 2.8 + 0.001*(a10 - 10);
		
		this.susp = a11 - h;
	}

}
