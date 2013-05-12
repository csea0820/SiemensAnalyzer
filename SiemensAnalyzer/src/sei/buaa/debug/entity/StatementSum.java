package sei.buaa.debug.entity;

public class StatementSum {
	
	private int lineNumber;
	private int a00;   
	private int a10;
	private int a01;
	private int a11;
	
	private double d_a11;
	private double d_a10;
	private double d_a01;
	private double d_a00;
	
	private double d_aef = 0.0;
	
	
	public StatementSum(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}
	
	
	public void addToDAef(double w)
	{
		d_aef += w;
	}
	
	public void addToDA00(double w)
	{
		d_a00 += w;
	}
	
	public void addToDA01(double w)
	{
		d_a01 += w;
	}
	
	public void addToDA10(double w)
	{
		d_a10 += w;
	}
	
	public void addToDA11(double w)
	{
		d_a11 += w;
	}
	
	public void incrementA10()
	{
		a10++;
	}
	
	public void incrementA11()
	{
		a11++;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public int getA00() {
		return a00;
	}
	public void setA00(int a00) {
		this.a00 = a00;
	}
	public int getA10() {
		return a10;
	}
	public void setA10(int a10) {
		this.a10 = a10;
	}
	public int getA01() {
		return a01;
	}
	public void setA01(int a01) {
		this.a01 = a01;
	}
	public int getA11() {
		return a11;
	}
	public void setA11(int a11) {
		this.a11 = a11;
	}
	
	
	@Override
	public String toString() {
		return "StatementSum [lineNumber=" + lineNumber + ", a00=" + a00
				+ ", a10=" + a10 + ", a01=" + a01 + ", a11=" + a11 + ", d_a11="
				+ d_a11 + ", d_a10=" + d_a10 + ", d_a01=" + d_a01 + ", d_a00="
				+ d_a00 + "]";
	}

	public double getD_a11() {
		return d_a11;
	}

	public void setD_a11(double d_a11) {
		this.d_a11 = d_a11;
	}

	public double getD_a10() {
		return d_a10;
	}

	public void setD_a10(double d_a10) {
		this.d_a10 = d_a10;
	}


	public double getD_a01() {
		return d_a01;
	}


	public void setD_a01(double d_a01) {
		this.d_a01 = d_a01;
	}


	public double getD_a00() {
		return d_a00;
	}


	public void setD_a00(double d_a00) {
		this.d_a00 = d_a00;
	}


	public double getD_aef() {
		return d_aef;
	}
}
