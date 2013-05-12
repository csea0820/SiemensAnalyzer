package sei.buaa.debug.metric;

public class WeightedJaccardSusp extends JaccardSusp {

	public WeightedJaccardSusp(int ln) {
		super(ln);
	}
	
	public void calcSups(int i_a00, int i_a01, int i_a10, int i_a11,double a00,double a01,double a10,double a11) {
		calcWeights(i_a00, i_a01, i_a10, i_a11,a00,a01,a10,a11);
		calcWeightedSups(d_a00, d_a01, d_a10, d_a11);
	}

}
