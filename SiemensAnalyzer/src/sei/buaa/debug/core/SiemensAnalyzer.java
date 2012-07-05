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
	Expensive WongExp = new Expensive(Constant.WONG);
	
	private int totalVersions;
	private int singleFaultsVersions;
	private int multiFaultsVersions;
	private int nonFaultVersions;
	private int analyzeVersions;
	
	private StringBuilder sb = new StringBuilder(5000);


	
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
		sb.append(String.format("%-10s","Interval")).append("\t")
		.append(String.format("%-10s", "Tarantula")).append("\t")
		.append(String.format("%-10s", "Jaccard")).append("\t")
		.append(String.format("%-10s", "Ochiai")).append("\t")
		.append(String.format("%-10s", "Sbi")).append("\t")
		.append(String.format("%-10s", "Wong")).append("\n");
		
		int a = 1;
		while (a != 11)
		{
			sb.append(String.format("%-10s",((a-1)*10+"-"+a*10))).append("\t");
			sb.append(String.format("%-10d",tarantulaExp.getIntervalNumber(a*1.0/10))).append("\t")
			.append(String.format("%-10d",jaccardExp.getIntervalNumber(a*1.0/10))).append("\t")
			.append(String.format("%-10d",ochiaiExp.getIntervalNumber(a*1.0/10))).append("\t")
			.append(String.format("%-10d",sbiExp.getIntervalNumber(a*1.0/10))).append("\t")
			.append(String.format("%-10d",WongExp.getIntervalNumber(a*1.0/10))).append("\n");
			a += 1;
		}
		
		FileUtility.writeContentToFile(sb.toString(), "/Users/csea/Documents/Experiment/Siemens/result/"+getCurrentDate()+".result");
		
	}
	
	private String getCurrentDate()
	{
		Date date=new Date();
		SimpleDateFormat formater=new SimpleDateFormat();
		formater.applyPattern("yyyy_MM_dd_HH_mm");
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

	public Expensive getWongExp() {
		return WongExp;
	}

}
