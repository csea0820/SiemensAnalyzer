package sei.buaa.debug.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import sei.buaa.debug.entity.Expensive;
import sei.buaa.debug.utility.Constant;
import sei.buaa.debug.utility.FileUtility;
import sei.buaa.debug.utility.StringUtility;

public class SiemensAnalyzer {

	Expensive tarantulaExp = new Expensive(Constant.TARANTULA);
	Expensive jaccardExp = new Expensive(Constant.JACCARD);
	Expensive ochiaiExp = new Expensive(Constant.OCHIAI);
	Expensive sbiExp = new Expensive(Constant.SBI);
	
	private int totalVersions;
	private int singleFaultsVersions;
	private int multiFaultsVersions;
	private int nonFaultVersions;
	private int analyzeVersions;
	
	private StringBuilder sb = new StringBuilder();


	
	public void analyze(String[] programPaths)
	{
		for (String path : programPaths)
		{
			System.out.println("analyzing program " + StringUtility.getBaseName(path));
			ProjectAnalyzer pa = new ProjectAnalyzer(this,path,false,false);
			pa.analyze();	
			totalVersions += pa.getTotalVersions();
			singleFaultsVersions += pa.getSingleFaultVersions();
			multiFaultsVersions += pa.getMultiFaultsVersions();
			nonFaultVersions += pa.getNonFaultVersions();
			analyzeVersions += pa.getAnalyzeVersions();
			sb.append(pa.getDiagnosisContent());
		}
		sb.append("Total Versions:" + totalVersions).append("\n");
		sb.append("NonFaultVersions :" + nonFaultVersions).append("\n");
		sb.append("SingleFaultsVersions :" + singleFaultsVersions).append("\n");
		sb.append("MultiFaultsVersions :" + multiFaultsVersions).append("\n");
		sb.append("AnalyzeVersions:"+analyzeVersions).append("\n");
		evaluation();
	}
	
	private void evaluation()
	{
		sb.append("I").append("\t").append("T").append("\t").append("J").append("\t")
							  .append("O").append("\t").append("S").append("\n");
		int a = 1;
		while (a != 11)
		{
			sb.append((a-1)*10).append("-").append(a*10).append("\t");
			sb.append(tarantulaExp.getIntervalNumber(a*1.0/10)).append("\t").append(jaccardExp.getIntervalNumber(a*1.0/10)).append("\t")
								.append(ochiaiExp.getIntervalNumber(a*1.0/10)).append("\t").append(sbiExp.getIntervalNumber(a*1.0/10)).append("\n");
			a += 1;
		}
		
		FileUtility.writeContentToFile(sb.toString(), "/Users/csea/Documents/Experiment/Siemens/result/"+getCurrentDate()+".result");
		
	}
	
	private String getCurrentDate()
	{
		Date date=new Date();
		SimpleDateFormat formater=new SimpleDateFormat();
		formater.applyPattern("yyyyMMddHHmm");
		return formater.format(date);
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

	public Expensive getSbiExp() {
		return sbiExp;
	}

}
