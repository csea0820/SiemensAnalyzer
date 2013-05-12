/**
 * csea 2012-12-3 下午11:42:08
 */
package sei.buaa.debug.metric;

/**
 * @author csea
 *
 */
public class VWTSusp extends AbstractSuspiciousness {

	double Nf,Ns;
	double Nf1,Nf2,Nf3;
	double Ns1,Ns2,Ns3;
	
	double wf1 = 1.0,wf2 = 0.1,wf3 = 0.01;
	double ws1 = 1.0,ws2 = 0.1;
	double alpha = 0.01;
	double Xfs = 0.0;
	
	public VWTSusp(int ln) {
		super(ln);
	}

	/* (non-Javadoc)
	 * @see sei.buaa.debug.metric.AbstractSuspiciousness#calcSups(int, int, int, int)
	 */
	@Override
	public void calcSups(int a00, int a01, int a10, int a11) {

	}
	
	public void calcSups(int i_a00, int i_a01, int i_a10, int i_a11,double a00,double a01,double a10,double a11){
		
//		double wtf = 0.0;
//		if (i_a11!= 0)
////			wtf = Math.log((i_a11+i_a10)*1.0/(i_a11)+1);
//			wtf = (i_a11+i_a10)*1.0/(i_a11);
//		a11 *= wtf;
//		a11 = i_a11;
		
//		double wnf = 0.0;
//		if (i_a01 != 0)
////			wnf = Math.log(i_a00*1.0/i_a01+2);
//			wnf = i_a00*1.0/i_a01+1;
//		a01 *= wnf;
//		a01 = i_a01;
		
		
//		double wtp = 0.0;
//		if (i_a10 != 0)
//			wtp = Math.log((i_a11+i_a10)*1.0/i_a10+1);
//		a10 *= wtp;
		a10 = i_a10;
		

		
//		double wnp = 0.0;
//		if (i_a00 != 0)
//			wnp = Math.log(((i_a00+i_a01)*1.0/i_a00)+1);
//		a00 *= wnp;
		a00 = i_a00;
		
//		System.out.println(lineNumber+":[a11="+i_a11+",d_a11="+a11+",a10="+i_a10+",d_a10="+a10+"]");
		
//		System.out.println("a11="+i_a11+",da11="+a11);
//		if (a10+a11 == 0)this.susp = 0;
//		else
//		{
//			double res = Math.sqrt((a11+a01)*(a11+a10)*1.0);
//			this.susp = a11/res;
//		}
		

		this.susp = a11-a10-a01;
		
//		this.susp = a11/(i_a11)-1;

	}

}
