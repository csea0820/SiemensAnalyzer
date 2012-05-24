package sei.buaa.debug.entity;

public abstract class Suspiciousness implements Comparable<Suspiciousness>{
	
	protected int lineNumber;
	protected double susp;
	
	
	
	public Suspiciousness(int ln)
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


	abstract public void calcSups(int a00,int a01,int a10,int a11);

	public void setSusp(double susp) {
		this.susp = susp;
	}
	
	public int compareTo(Suspiciousness arg0) {
		
		return this.getSusp() > arg0.getSusp() ? 0: 1;
	}
	
	public String toString()
	{
		return "[lineNumber="+lineNumber+",susp="+susp+"]";
	}

}
