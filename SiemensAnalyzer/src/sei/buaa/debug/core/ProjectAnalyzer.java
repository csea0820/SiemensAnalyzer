package sei.buaa.debug.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sei.buaa.debug.entity.Expensive;
import sei.buaa.debug.entity.Statement;
import sei.buaa.debug.entity.StatementSum;
import sei.buaa.debug.entity.TestCase;
import sei.buaa.debug.entity.Timer;
import sei.buaa.debug.entity.Version;
import sei.buaa.debug.metric.AmpleSusp;
import sei.buaa.debug.metric.ISuspsCalculator;
import sei.buaa.debug.metric.JaccardSusp;
import sei.buaa.debug.metric.LeeAmpleSusp;
import sei.buaa.debug.metric.LeeJaccardSusp;
import sei.buaa.debug.metric.LeeOchiaiSusp;
import sei.buaa.debug.metric.LeeOpSusp;
import sei.buaa.debug.metric.LeeSBISusp;
import sei.buaa.debug.metric.LeeTarantulaSusp;
import sei.buaa.debug.metric.LeeWong3Susp;
import sei.buaa.debug.metric.OchiaiSusp;
import sei.buaa.debug.metric.OpSusp;
import sei.buaa.debug.metric.RA1Susp;
import sei.buaa.debug.metric.SBISusp;
import sei.buaa.debug.metric.SIQSusp;
import sei.buaa.debug.metric.AbstractSuspiciousness;
import sei.buaa.debug.metric.TarantulaSusp;
import sei.buaa.debug.metric.WeightedAmpleSusp;
import sei.buaa.debug.metric.WeightedJaccardSusp;
import sei.buaa.debug.metric.WeightedOchiaiSusp;
import sei.buaa.debug.metric.WeightedOpSusp;
import sei.buaa.debug.metric.WeightedSBISusp;
import sei.buaa.debug.metric.WeightedTarantulSusp;
import sei.buaa.debug.metric.WeightedWong2Susp;
import sei.buaa.debug.metric.WeightedWong3Susp;
import sei.buaa.debug.metric.Wong2Susp;
import sei.buaa.debug.metric.Wong3Susp;
import sei.buaa.debug.metric.WongSusp;
import sei.buaa.debug.utility.Constant;
import sei.buaa.debug.utility.StringUtility;

public class ProjectAnalyzer {

	Map<Integer, List<Integer>> faults = null;
	List<Integer> versions = null;
	String programName;
	String programDir;

	private int totalVersions;
	private int singleFaultVersionsCnt;
	private int multiFaultsVersionsCnt;
	private int nonFaultVersionsCnt;
	private int analyzeVersionsCnt;
	int versionsOfIndividualProgramsCnt = 0;
	int totalTestsCnt = 0;
	private boolean coincidentalCorrectnessEnable = false;
	private boolean coincidentalCorrectnessAbandon = false;
	private StringBuilder diagnosisContent = new StringBuilder(10000);

	private StringBuilder expenseSummary = null;
	private double expenseEfforts[] = new double [12];
	private int totalCodeCnt = -1;
	
	SiemensAnalyzer sa;

	public ProjectAnalyzer(SiemensAnalyzer sa, String programDir,
			boolean coincidentalCorrectnessEnable,
			boolean coincidentalCorrectnessAbandon) {
		this.coincidentalCorrectnessEnable = coincidentalCorrectnessEnable;
		this.coincidentalCorrectnessAbandon = coincidentalCorrectnessAbandon;
		this.programDir = programDir;
		this.sa = sa;
		this.expenseSummary = sa.getExpenseSummary();
		totalVersions = 0;
		singleFaultVersionsCnt = 0;
		nonFaultVersionsCnt = 0;
		analyzeVersionsCnt = 0;
		programName = StringUtility.getBaseName(programDir);
		faults = new HashMap<Integer, List<Integer>>();
		versions = new ArrayList<Integer>();
		init();
	}

	private void init() {
		getVersionsInfo(programDir + Constant.VERSIONS_LIST);
		getFaultLocation(programDir + Constant.FAULTS_LIST);
	}

	public void analyze() {
		versionsOfIndividualProgramsCnt = 0;
		totalTestsCnt = 0;
		Arrays.fill(expenseEfforts, 0);
		totalCodeCnt = 0;
		for (int vid : versions) {
			Parser parser = new Parser();
			Version v = new Version();
			v.setName(programName);
			v.setVersionId(vid);
			v.addFaults(faults);
			parser.setFaults(v.getFaults());
			totalVersions++;
//			System.out.println("version:"+vid);

			diagnosisContent.append("\n");
			if (v.getFaultNumber() > 1) {
				multiFaultsVersionsCnt++;
				diagnosisContent.append(
						"program=" + programName + ",version=" + vid
								+ " is a mutil-faults version").append("\n");
				continue;
			} else if (v.getFaultNumber() < 1) {
				nonFaultVersionsCnt++;
				diagnosisContent.append(
						"program=" + programName + ",version=" + vid
								+ " is a non-fault version").append("\n");
				continue;
			} else
				singleFaultVersionsCnt++;

			Map<Integer, StatementSum> stSum = parser.parser(programDir + "/"
					+ Constant.OUT_PUT_DIR + "/v" + vid);
//			if (coincidentalCorrectnessEnable)
//				list = parser.addCoincidentalCorrectnessInfo(list, programDir
//						+ "/" + Constant.COINCIDENTAL_CORRECTNESS_DIR
//						+ "coincidentalCorrectness." + vid);
			v.setTotalFailedCount(parser.getTotalFailedTestCaseCnt());
			v.setTotalPassedCount(parser.getTotalPassedTestCaseCnt());
			v.setTotalExecutableCode(parser.getTotalExecutableCodeCnt());
			v.setTotalWeights(parser.getTotalWeights());
				
			analyzeVersion(v, stSum,v.getTotalWeights());
		}
		
		addExpenseSummary(programName);
		
		System.out.println("Program_Name:"+programName+",analyzeVersions:"+versionsOfIndividualProgramsCnt
				+",totalTests:"+totalTestsCnt);
	}
	
	private void addExpenseSummary(String subject)
	{
		expenseSummary.append(subject).append(",");
		for (double v : expenseEfforts)
		{
			expenseSummary.append(String.format("%5.0f",v*100/analyzeVersionsCnt)).append("%,");
		}
		expenseSummary.replace(expenseSummary.length()-1, expenseSummary.length(), "\n");
	}

	public boolean isPassed(TestCase t) {
		// if (coincidentalCorrectnessEnable)
		// {
		// if (t.isPassed() && t.isCoincidentalCorrectness())
		// return false;
		// }

		return t.isPassed();
	}

	public void analyzeVersion(Version v, Map<Integer, StatementSum> map,double totalWeights) {

		if (v.getTotalFailedCount() == 0) {
			diagnosisContent.append(
					"program=" + programName + ",version=" + v.getVersionId()
							+ " has no failed test cases").append("\n");
			return;
		}
		
		totalCodeCnt += v.getTotalExecutableCode();
		
		analyzeVersionsCnt++;
		versionsOfIndividualProgramsCnt++;
		totalTestsCnt = v.getTotalFailedCount()+v.getTotalPassedCount();
		diagnosisContent.append(
				"\n\nanalyzing program " + programName + ",verions "
						+ v.getVersionId()).append("\n");
		diagnosisContent.append(v.getVersionInfo()+"\n");

		List<AbstractSuspiciousness> tarantulaSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedTarantulaSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeTarantulaSusps = new ArrayList<AbstractSuspiciousness>();

		List<AbstractSuspiciousness> jaccardSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedJaccardSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeJaccardSusps = new ArrayList<AbstractSuspiciousness>();

		List<AbstractSuspiciousness> ochiaiSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedOchiaiSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeOchiaiSusps = new ArrayList<AbstractSuspiciousness>();
		
		List<AbstractSuspiciousness> sbiSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedSbiSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeSbiSusps = new ArrayList<AbstractSuspiciousness>();

		List<AbstractSuspiciousness> wongSusps = new ArrayList<AbstractSuspiciousness>();
		
		List<AbstractSuspiciousness> siqSusps = new ArrayList<AbstractSuspiciousness>();
		
		List<AbstractSuspiciousness> ra1Susps = new ArrayList<AbstractSuspiciousness>();
		
		
		List<AbstractSuspiciousness> wong2Susps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedWong2Susps = new ArrayList<AbstractSuspiciousness>();

		List<AbstractSuspiciousness> ampleSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedAmpleSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeAmpleSusps = new ArrayList<AbstractSuspiciousness>();


		List<AbstractSuspiciousness> opSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedOpSusps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeOpSusps = new ArrayList<AbstractSuspiciousness>();

		List<AbstractSuspiciousness> wong3Susps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> weightedWong3Susps = new ArrayList<AbstractSuspiciousness>();
		List<AbstractSuspiciousness> leeWong3Susps = new ArrayList<AbstractSuspiciousness>();


		for (StatementSum eSum : map.values()) {
			eSum.setA00(v.getTotalPassedCount() - eSum.getA10());
			eSum.setA01(v.getTotalFailedCount() - eSum.getA11());

//  		System.out.println(eSum);
			
			//Tarantula
			addSuspToList(tarantulaSusps,new TarantulaSusp(),eSum);
			addSuspToList(weightedTarantulaSusps,new WeightedTarantulSusp(),eSum);
			addSuspToList(leeTarantulaSusps,new LeeTarantulaSusp(totalWeights),eSum);
			
			//Jaccard
			addSuspToList(jaccardSusps,new JaccardSusp(),eSum);
			addSuspToList(weightedJaccardSusps,new WeightedJaccardSusp(),eSum);
			addSuspToList(leeJaccardSusps,new LeeJaccardSusp(totalWeights),eSum);

			//Ochiai
			addSuspToList(ochiaiSusps,new OchiaiSusp(),eSum);
			addSuspToList(weightedOchiaiSusps,new WeightedOchiaiSusp(),eSum);
			addSuspToList(leeOchiaiSusps,new LeeOchiaiSusp(totalWeights),eSum);
			
			
			//SBI
			addSuspToList(sbiSusps,new SBISusp(),eSum);
			addSuspToList(weightedSbiSusps,new WeightedSBISusp(),eSum);
			addSuspToList(leeSbiSusps,new LeeSBISusp(totalWeights),eSum);			
			
			//Wong
			addSuspToList(wongSusps,new WongSusp(),eSum);
			
			//Wong2
			addSuspToList(wong2Susps,new Wong2Susp(),eSum);
			addSuspToList(weightedWong2Susps,new WeightedWong2Susp(),eSum);
			
			//wong3
			addSuspToList(wong3Susps,new Wong3Susp(),eSum);
			addSuspToList(weightedWong3Susps,new WeightedWong3Susp(),eSum);
			addSuspToList(leeWong3Susps,new LeeWong3Susp(totalWeights),eSum);
			
			//SIQ
			addSuspToList(siqSusps,new SIQSusp(),eSum);
	
			//RA1
			addSuspToList(ra1Susps,new RA1Susp(),eSum);
	
			//Ample
			addSuspToList(ampleSusps,new AmpleSusp(),eSum);
			addSuspToList(weightedAmpleSusps,new WeightedAmpleSusp(),eSum);
			addSuspToList(leeAmpleSusps,new LeeAmpleSusp(totalWeights),eSum);				
			//Op
			addSuspToList(opSusps,new OpSusp(),eSum);
			addSuspToList(weightedOpSusps,new WeightedOpSusp(),eSum);
			addSuspToList(leeOpSusps,new LeeOpSusp(totalWeights),eSum);			
		}
		
		
		diagnosisContent.append(v.getFaultInfo(map));
		
		rank(v, tarantulaSusps, TarantulaSusp.class.getSimpleName(),
				sa.getTarantulaExp(), map);
		//System.out.println(v.getExpensive());
		expenseEfforts[0] += v.getExpensive();
		rank(v, weightedTarantulaSusps, WeightedTarantulSusp.class.getSimpleName(),
				sa.getWeightedTarantulaExp(), map);
		expenseEfforts[1]+= v.getExpensive();
		rank(v, leeTarantulaSusps, LeeTarantulaSusp.class.getSimpleName(),
				sa.getLeeTarantulaExp(), map);
		expenseEfforts[2]+= v.getExpensive();
		
		
		rank(v, jaccardSusps, JaccardSusp.class.getSimpleName(),
				sa.getJaccardExp(), map);
		expenseEfforts[3] += v.getExpensive();
		rank(v, weightedJaccardSusps, WeightedJaccardSusp.class.getSimpleName(),
				sa.getWeightedJaccardExp(), map);
		expenseEfforts[4] += v.getExpensive();
		rank(v, leeJaccardSusps, LeeJaccardSusp.class.getSimpleName(),
				sa.getLeeJaccardExp(), map);
		expenseEfforts[5] += v.getExpensive();
	
		rank(v, ochiaiSusps, OchiaiSusp.class.getSimpleName(),
				sa.getOchiaiExp(), map);
		expenseEfforts[6] += v.getExpensive();
		rank(v, weightedOchiaiSusps, WeightedOchiaiSusp.class.getSimpleName(),
				sa.getWeightedOchiaiExp(), map);
		expenseEfforts[7] += v.getExpensive();
		rank(v, leeOchiaiSusps, LeeOchiaiSusp.class.getSimpleName(),
				sa.getLeeOchiaiExp(), map);
		expenseEfforts[8] += v.getExpensive();
		
		rank(v, sbiSusps, SBISusp.class.getSimpleName(), sa.getSbiExp(), map);
		expenseEfforts[9] += v.getExpensive();
		rank(v, weightedSbiSusps, WeightedSBISusp.class.getSimpleName(), sa.getWeightedSbiExp(), map);
		expenseEfforts[10] += v.getExpensive();
		rank(v, leeSbiSusps, LeeSBISusp.class.getSimpleName(), sa.getLeeSbiExp(), map);
		expenseEfforts[11] += v.getExpensive();
		
		rank(v, wongSusps, WongSusp.class.getSimpleName(), sa.getWongExp(), map);
		
		rank(v, wong3Susps, Wong3Susp.class.getSimpleName(), sa.getWong3Exp(), map);
		rank(v, weightedWong3Susps, WeightedWong3Susp.class.getSimpleName(), sa.getWeightedWong3Exp(), map);
		rank(v, leeWong3Susps, LeeWong3Susp.class.getSimpleName(), sa.getLeeWong3Exp(), map);

		
		rank(v, wong2Susps, Wong2Susp.class.getSimpleName(), sa.getWong2Exp(), map);
		rank(v, weightedWong2Susps, WeightedWong2Susp.class.getSimpleName(), sa.getWeightedWong2Exp(), map);
	
		rank(v, siqSusps, SIQSusp.class.getSimpleName(), sa.getSiqExp(), map);
		
		rank(v, ra1Susps, RA1Susp.class.getSimpleName(), sa.getRaExp(), map);
		
		rank(v, ampleSusps, AmpleSusp.class.getSimpleName(), sa.getAmpleExp(), map);
		rank(v, weightedAmpleSusps, WeightedAmpleSusp.class.getSimpleName(), sa.getWeightedAmpleExp(), map);
		rank(v, leeAmpleSusps, LeeAmpleSusp.class.getSimpleName(), sa.getLeeAmpleExp(), map);

		rank(v, opSusps, OpSusp.class.getSimpleName(), sa.getOpExp(), map);
		rank(v, weightedOpSusps, WeightedOpSusp.class.getSimpleName(), sa.getWeightedOpExp(), map);
		rank(v, leeOpSusps, LeeOpSusp.class.getSimpleName(), sa.getLeeOpExp(), map);
		
		

	}

	private int rank(Version v, List<AbstractSuspiciousness> susp, String fl,
			Expensive exp, Map<Integer, StatementSum> map) {
		Collections.sort(susp);

		v.calcExamineEffort(susp);
		v.writeResultToFile(susp, programDir + "/FL", fl);
		exp.addExpensive(v.getExpensive());
		v.setTechnique(fl);
		diagnosisContent.append(v);
		StatementSum sum = map.get(susp.get(0).getLineNumber());
		if (sum != null)
			diagnosisContent
					.append("\nMostSuspStatement:" + sum.getLineNumber()).append("\n")
					.append(sum).append("\n\n");
		
		return v.getExamineEffort();
	}

	public void getFaultLocation(String faultFile) {
		BufferedReader br = null;
		FileReader fr = null;
		String str;
		try {
			fr = new FileReader(faultFile);
			br = new BufferedReader(fr);

			str = br.readLine();
			while (str != null) {
				String[] nums = str.split("\\s+");
				int versionId = Integer.parseInt(nums[0]);
				List<Integer> fault = new ArrayList<Integer>();
				for (int i = 1; i < nums.length; i++) {
					fault.add(Integer.parseInt(nums[i]));
				}

				faults.put(versionId, fault);
				// read another line
				str = br.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// for (Integer key : faults.keySet())
		// {
		// for (Integer value : faults.get(key))
		// {
		// System.out.println("version="+key+",fault="+value);
		// }
		// }
	}

	private void addSuspToList(List<AbstractSuspiciousness> list,ISuspsCalculator suspsCalc,StatementSum eSum)
	{
		AbstractSuspiciousness susp = new AbstractSuspiciousness(suspsCalc);
		susp.calcSups(eSum);
		susp.setLineNumber(eSum.getLineNumber());
		list.add(susp);
	}
	
	private void getVersionsInfo(String versionFile) {
		BufferedReader bufferReader = null;
		String str = null;

		try {
			bufferReader = new BufferedReader(new FileReader(versionFile));
			str = bufferReader.readLine();

			while (str != null) {
				versions.add(Integer.parseInt(str));
				str = bufferReader.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
	}

	public int getTotalVersions() {
		return totalVersions;
	}

	public int getSingleFaultVersions() {
		return singleFaultVersionsCnt;
	}

	public int getMultiFaultsVersions() {
		return multiFaultsVersionsCnt;
	}

	public int getNonFaultVersions() {
		return nonFaultVersionsCnt;
	}

	public int getAnalyzeVersions() {
		return analyzeVersionsCnt;
	}

	public StringBuilder getDiagnosisContent() {
		return diagnosisContent;
	}

}
