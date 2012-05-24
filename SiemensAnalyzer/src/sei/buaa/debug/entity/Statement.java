package sei.buaa.debug.entity;

public class Statement {
	
	private int lineNumber;
	private int count;
	
	public Statement(int lineNumber,int count)
	{
		this.lineNumber = lineNumber;
		this.count = count;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int isExecutedOrNot()
	{
		return count == 0 ? 0:1;
	}	
}
