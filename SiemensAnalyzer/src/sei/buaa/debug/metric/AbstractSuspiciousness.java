package sei.buaa.debug.metric;

import java.util.List;

public abstract class AbstractSuspiciousness implements Comparable<AbstractSuspiciousness>{
	
	protected int lineNumber;
	protected double susp;
	
	List<AbstractSuspiciousness> list = null;
	
	
	double d_a00,d_a01,d_a10,d_a11;
	
	double l_a00,l_a01,l_a10,l_a11;
	
	public void calcWeights(int i_a00, int i_a01, int i_a10, int i_a11,double a00,double a01,double a10,double a11){

		double wtf = 0.0;
		if (i_a11 != 0)
			 wtf = Math.log((i_a11+i_a10)*1.0/(i_a11))*(i_a11*1.0/(i_a01+i_a10));
			//wtf = (i_a11 + i_a10) * 1.0 / (i_a11);
			//wtf = (i_a00+i_a01+i_a10+i_a11)*1.0/(i_a01+i_a11);
			//wtf = (Math.log(i_a00*1.0/i_a11+2));
			//wtf = i_a00*1.0 / i_a11 +1;
		d_a11 = a11*wtf;
		//d_a11 = i_a11;

		double wnf = 0.0;
		if (i_a01 != 0)
			 wnf = Math.log(i_a00*1.0/i_a01+2);
			//wnf = i_a00 * 1.0 / i_a01 + 1;
			//wnf = (i_a00+i_a01+i_a10+i_a11)*1.0/(i_a01+i_a11);
		d_a01 = a01*wnf;
		//d_a01 = i_a01;

		d_a10 = i_a10;

		d_a00 = i_a00;

	}
	
	public void calcLeeWeights(int i_a00, int i_a01, int i_a10,int i_a11,double d_aef,double totalWeights)
	{
		l_a11 = d_aef*(i_a01+i_a11)/totalWeights;
		l_a01 = (i_a01+i_a11)-l_a11;
		l_a00 = i_a00;
		l_a10 = i_a10;
	}
	
	public AbstractSuspiciousness(int ln)
	{
		lineNumber = ln;
	}


	public int getLineNumber() {
		return lineNumber;
	}



	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}



	public double getSusp() {
		return susp;
	}

	/**
	 * @param a00 test case that don't execute the statement is classified as passes
	 * @param a01 test case that don't execute the statement is classified as fails
	 * @param a10 test case that execute the statement is classified as passes
	 * @param a11 test case that execute the statement is classified as fails
	 * 
	 */
	abstract public void calcSups(int a00,int a01,int a10,int a11);
	
	public void calcWeightedSups(double a00, double a01,double a10, double a11)
	{
		
	}

	public void setSusp(double susp) {
		this.susp = susp;
	}
	
	public int compareTo(AbstractSuspiciousness arg0) {
		
		return this.getSusp() > arg0.getSusp() ? 0: 1;
	}
	
	public String toString()
	{
		return "[lineNumber="+lineNumber+",susp="+susp+"]";
	}

}
