package sei.buaa.debug.core;

import sei.buaa.debug.entity.Expensive;
import sei.buaa.debug.utility.Constant;
import sei.buaa.debug.utility.StringUtility;

public class SiemensAnalyzer {

	Expensive tarantulaExp = new Expensive(Constant.TARANTULA);
	Expensive jaccardExp = new Expensive(Constant.JACCARD);
	Expensive ochiaiExp = new Expensive(Constant.OCHIAI);
	
	private int totalVersions;
	private int availableVersions;
	
	public void analyze(String[] programPaths)
	{
		for (String path : programPaths)
		{
			System.out.println("analyzing program " + StringUtility.getBaseName(path));
			ProjectAnalyzer pa = new ProjectAnalyzer(this,path);
			pa.analyze();	
			totalVersions += pa.getTotalVersions();
			availableVersions += pa.getAvailableVersions();
		}
		System.out.println("Total Versions:" + totalVersions);
		System.out.println("AvailableVersions :" + availableVersions);
		evaluation();
	}
	
	private void evaluation()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("I").append("\t").append("T").append("\t").append("J").append("\t")
							  .append("O").append("\n");
		int a = 1;
		while (a != 11)
		{
			sb.append((a-1)*10).append("-").append(a*10).append("\t");
			sb.append(tarantulaExp.getIntervalNumber(a*1.0/10)).append("\t").append(jaccardExp.getIntervalNumber(a*1.0/10)).append("\t")
								.append(ochiaiExp.getIntervalNumber(a*1.0/10)).append("\n");
			a += 1;
		}
		
		System.out.println(sb.toString());
		
		
	}
	
	public Expensive getTarantulaExp() {
		return tarantulaExp;
	}

	public Expensive getJaccardExp() {
		return jaccardExp;
	}

	public Expensive getOchiaiExp() {
		return ochiaiExp;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 0)
		{
			SiemensAnalyzer sa = new SiemensAnalyzer();
			sa.analyze(args);
		}
	}

}
