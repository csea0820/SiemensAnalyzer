package sei.buaa.debug.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import sei.buaa.debug.entity.Expensive;
import sei.buaa.debug.utility.Constant;
import sei.buaa.debug.utility.FileUtility;
import sei.buaa.debug.utility.StringUtility;

public class SiemensAnalyzer {

	private StringBuilder expenseSummary = new StringBuilder(1000);
	
	Expensive tarantulaExp = new Expensive(Constant.TARANTULA);
	Expensive wtarantulaExp = new Expensive(Constant.WTARANTULA);
	Expensive ltarantulaExp = new Expensive(Constant.LTARANTULA);


	Expensive jaccardExp = new Expensive(Constant.JACCARD);
	Expensive wjaccardExp = new Expensive(Constant.WJACCARD);
	Expensive ljaccardExp = new Expensive(Constant.LJACCARD);


	Expensive ochiaiExp = new Expensive(Constant.OCHIAI);
	Expensive wochiaiExp = new Expensive(Constant.WOCHIAI);
	Expensive lochiaiExp = new Expensive(Constant.LOCHIAI);


	
	Expensive sbiExp = new Expensive(Constant.SBI);
	Expensive wsbiExp = new Expensive(Constant.WSBI);
	Expensive lsbiExp = new Expensive(Constant.LSBI);

	
	Expensive WongExp = new Expensive(Constant.WONG);
	
	Expensive wong2Exp = new Expensive(Constant.WONG2);
	Expensive wwong2Exp = new Expensive(Constant.WWONG2);

	Expensive siqExp = new Expensive(Constant.SIQ);
	
	Expensive raExp = new Expensive(Constant.RA1);
	
	Expensive vwtExp = new Expensive(Constant.VWT);
	
	Expensive ampleExp = new Expensive(Constant.AMPLE);
	Expensive lampleExp = new Expensive(Constant.LAMPLE);
	Expensive wampleExp = new Expensive(Constant.WAMPLE);

	Expensive opExp = new Expensive(Constant.OP);
	Expensive wopExp = new Expensive(Constant.WOP);
	Expensive lopExp = new Expensive(Constant.LOP);
	
	Expensive wong3Exp = new Expensive(Constant.WONG3);
	Expensive wwong3Exp = new Expensive(Constant.WWONG3);
	Expensive lwong3Exp = new Expensive(Constant.LWONG3);


	
	private int totalVersions;
	private int singleFaultsVersions;
	private int multiFaultsVersions;
	private int nonFaultVersions;
	private int analyzeVersions;
	
	private StringBuilder sb = new StringBuilder(5000);
	
	public void analyze(String[] programPaths)
	{
		expenseSummary.append("Subject,Tarantula,WTarantula,LTarantula,Jaccard,WJaccard,LJaccard,Ochiai,WOchiai,LOchiai,Ochiai," +
				"Sbi,WSbi,LSbi").append("\n");
		
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
			System.gc();
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
		.append(String.format("%-10s", "WTarantula")).append("\t")
		.append(String.format("%-10s", "LTarantula")).append("\t")

		.append(String.format("%-10s", "Jaccard")).append("\t")
		.append(String.format("%-10s", "WJaccard")).append("\t")
		.append(String.format("%-10s", "LJaccard")).append("\t")

		.append(String.format("%-10s", "Ochiai")).append("\t")
		.append(String.format("%-10s", "WOchiai")).append("\t")
		.append(String.format("%-10s", "LOchiai")).append("\t")

		.append(String.format("%-10s", "Sbi")).append("\t")
		.append(String.format("%-10s", "WSbi")).append("\t")
		.append(String.format("%-10s", "LSbi")).append("\t")

		.append(String.format("%-10s", "Wong")).append("\t")
		
		.append(String.format("%-10s", "Wong2")).append("\t")
		.append(String.format("%-10s", "WWong2")).append("\t")
		
		.append(String.format("%-10s", "wong3")).append("\t")
		.append(String.format("%-10s", "wwong3")).append("\t")
		.append(String.format("%-10s", "lwong3")).append("\t")

		.append(String.format("%-10s", "SIQ")).append("\t")
		
		.append(String.format("%-10s", "RA")).append("\t")
		
		.append(String.format("%-10s", "VWT")).append("\t")

		
		.append(String.format("%-10s", "Ample")).append("\t")
		.append(String.format("%-10s", "WAmple")).append("\t")
		.append(String.format("%-10s", "LAmple")).append("\t")

		.append(String.format("%-10s", "Op")).append("\t")
		.append(String.format("%-10s", "WOp")).append("\t")
		.append(String.format("%-10s", "LOp")).append("\n");		

		int a = 1;
		int interval = 5;
		while (a <= (100/interval))
		{
			sb.append(String.format("%-10s",((a-1)*interval+"-"+a*interval))).append("\t");
			sb.append(String.format("%-10d",tarantulaExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wtarantulaExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",ltarantulaExp.getIntervalNumber(a*0.01*interval))).append("\t")

			
			.append(String.format("%-10d",jaccardExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wjaccardExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",ljaccardExp.getIntervalNumber(a*0.01*interval))).append("\t")

			.append(String.format("%-10d",ochiaiExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wochiaiExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",lochiaiExp.getIntervalNumber(a*0.01*interval))).append("\t")
			
			.append(String.format("%-10d",sbiExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wsbiExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",lsbiExp.getIntervalNumber(a*0.01*interval))).append("\t")

			.append(String.format("%-10d",WongExp.getIntervalNumber(a*0.01*interval))).append("\t")
			
			.append(String.format("%-10d",wong2Exp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wwong2Exp.getIntervalNumber(a*0.01*interval))).append("\t")

			.append(String.format("%-10d",wong3Exp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wwong3Exp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",lwong3Exp.getIntervalNumber(a*0.01*interval))).append("\t")
			
			.append(String.format("%-10d",siqExp.getIntervalNumber(a*0.01*interval))).append("\t")

			.append(String.format("%-10d",raExp.getIntervalNumber(a*0.01*interval))).append("\t")

			.append(String.format("%-10d",vwtExp.getIntervalNumber(a*0.01*interval))).append("\t")
			
			.append(String.format("%-10d",ampleExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wampleExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",lampleExp.getIntervalNumber(a*0.01*interval))).append("\t")
			
			.append(String.format("%-10d",opExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",wopExp.getIntervalNumber(a*0.01*interval))).append("\t")
			.append(String.format("%-10d",lopExp.getIntervalNumber(a*0.01*interval))).append("\n");
			
			a += 1;
		}
		
		System.out.println(sb.toString());
		FileUtility.writeContentToFile(sb.toString(), "/Users/csea/Documents/Experiment/Siemens/result/"+getCurrentDate()+".result");
		System.out.println(expenseSummary.toString());
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

	public Expensive getSiqExp() {
		return siqExp;
	}

	public Expensive getRaExp() {
		return raExp;
	}

	public Expensive getVwtExp() {
		return vwtExp;
	}

	public Expensive getWong2Exp() {
		return wong2Exp;
	}

	public Expensive getAmpleExp() {
		return ampleExp;
	}

	public Expensive getOpExp() {
		return opExp;
	}


	public Expensive getWeightedTarantulaExp() {
		return wtarantulaExp;
	}

	public Expensive getWeightedJaccardExp() {
		return wjaccardExp;
	}

	public Expensive getWeightedOchiaiExp() {
		return wochiaiExp;
	}

	public Expensive getWeightedSbiExp() {
		return wsbiExp;
	}

	public Expensive getWeightedWong2Exp() {
		return wwong2Exp;
	}

	public Expensive getWeightedAmpleExp() {
		return wampleExp;
	}

	public Expensive getWeightedOpExp() {
		return wopExp;
	}

	public Expensive getLeeTarantulaExp() {
		return ltarantulaExp;
	}

	public Expensive getLeeJaccardExp() {
		return ljaccardExp;
	}

	public Expensive getLeeOchiaiExp() {
		return lochiaiExp;
	}

	public Expensive getLeeSbiExp() {
		return lsbiExp;
	}

	public Expensive getLeeAmpleExp() {
		return lampleExp;
	}

	public Expensive getLeeOpExp() {
		return lopExp;
	}

	public Expensive getWong3Exp() {
		return wong3Exp;
	}

	public Expensive getWeightedWong3Exp() {
		return wwong3Exp;
	}

	public Expensive getLeeWong3Exp() {
		return lwong3Exp;
	}

	public StringBuilder getExpenseSummary() {
		return expenseSummary;
	}

	public void setExpenseSummary(StringBuilder expenseSummary) {
		this.expenseSummary = expenseSummary;
	}

}
