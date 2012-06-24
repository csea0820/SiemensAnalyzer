package sei.buaa.debug.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sei.buaa.debug.utility.FileUtility;

public class Version {
	
	private int totalExecutableCode;
	private int versionId;
	private String name;
	private int totalPassedCount;
	private int totalFailedCount;
	List<Integer> faults = null;
	private int examineEffort;
	private double expensive;
	private String technique;
	
	
	public Version()
	{
		faults = new ArrayList<Integer>();
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
	
	public String getFaultInfo(Map<Integer,StatementSum> map)
	{
		String res = "faultLocation:"+faults.get(0);
		StatementSum sum = map.get(faults.get(0));
		if (sum != null)
		{
			res += " ExecutionInfo:[a00="+sum.getA00()+",a10="+sum.getA10()+",a01="+sum.getA01()+",a11="+sum.getA11()+"]";
		}
		return res+"\n";
	}
	
	public int getFault()
	{
		Iterator<Integer> iterator = faults.iterator();
		return iterator.next();
	}
	
	public int getFaultNumber()
	{
		return faults.size();
	}

	public int getTotalPassedCount() {
		return totalPassedCount;
	}

	public int getTotalFailedCount() {
		return totalFailedCount;
	}
	
	public void addFaults(Map<Integer,List<Integer> > map)
	{
		for (int f: map.get(versionId))
			faults.add(f);
	}
	
	public void calcExamineEffort(List<Suspiciousness> list)
	{
		examineEffort = 0;
		for (Suspiciousness s: list)
		{
			examineEffort++;
			if (s.getLineNumber() == faults.get(0))
				break;
		}
		expensive = examineEffort*1.0/totalExecutableCode;
	}
	
	public void writeResultToFile(List<Suspiciousness> list,String path,String fl)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("lineNumber").append("\t").append("suspiciousness\n");
		for (Suspiciousness s: list)
		{
			sb.append(s.getLineNumber()).append("\t").append(s.getSusp()).append("\n");
		}
		FileUtility.writeContentToFile(sb.toString(),path+"/"+fl+"_v"+versionId);
	}
	
	public String toString()
	{
		return  "[technique="+technique+
				",program="+name+",version="+versionId+",totalPassedCount="
				+totalPassedCount+",totalFailedCoount="+totalFailedCount+",examineEffort="+examineEffort+
				",expensive="+examineEffort*1.0/totalExecutableCode+"]";
	}

	public int getTotalExecutableCode() {
		return totalExecutableCode;
	}

	public void setTotalExecutableCode(int totalExecutableCode) {
		this.totalExecutableCode = totalExecutableCode;
	}

	public double getExpensive() {
		return expensive;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}

}
