package sei.buaa.debug.metric;

import java.util.List;

public abstract class AbstractSuspiciousness implements Comparable<AbstractSuspiciousness>{
	
	protected int lineNumber;
	protected double susp;
	
	List<AbstractSuspiciousness> list = null;
	
	
	
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

	/*
	 * a00:test case that don't execute the statement passes
	 * a01:test case that don't execute the statement fails
	 * a10:test case that execute the statement passes
	 * a11:test case that execute the statement fails
	 */
	abstract public void calcSups(int a00,int a01,int a10,int a11);

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
