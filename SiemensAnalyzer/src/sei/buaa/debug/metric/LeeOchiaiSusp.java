package sei.buaa.debug.metric;

public class LeeOchiaiSusp extends OchiaiSusp {

	public LeeOchiaiSusp(int ln) {
		super(ln);
	}
	
	public void calcSups(int i_a00, int i_a01, int i_a10, int i_a11,double d_aef,double totalWeights) 
	{
		calcLeeWeights(i_a00, i_a01, i_a10, i_a11, d_aef, totalWeights);
		calcWeightedSups(l_a00, l_a01, l_a10, l_a11);
	}

}
