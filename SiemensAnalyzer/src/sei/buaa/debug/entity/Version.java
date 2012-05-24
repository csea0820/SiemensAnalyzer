package sei.buaa.debug.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Version {
	
	private int versionId;
	private String name;
	private int totalPassedCount;
	private int totalFailedCount;
	Set<Integer> faults = null;
	
	
	public Version()
	{
		faults = new HashSet<Integer>();
	}
	
	public void passedIncrement()
	{
		totalPassedCount++;
	}
	public void failedIncrement()
	{
		totalFailedCount++;
	}
	
	public int getVersionId() {
		return versionId;
	}
	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addFault(int lineNumber)
	{
		faults.add(lineNumber);
	}
	
	public int getFault()
	{
		Iterator<Integer> iterator = faults.iterator();
		return iterator.next();
	}

	public int getTotalPassedCount() {
		return totalPassedCount;
	}

	public int getTotalFailedCount() {
		return totalFailedCount;
	}
	
	public String toString()
	{
		return "[totalPassedCount="+totalPassedCount+",totalFailedCoount="+totalFailedCount+"]";
	}

}
