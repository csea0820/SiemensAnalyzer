package sei.buaa.debug.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sei.buaa.debug.entity.Statement;
import sei.buaa.debug.entity.StatementSum;
import sei.buaa.debug.entity.TestCase;
import sei.buaa.debug.entity.Timer;
import sei.buaa.debug.utility.FileUtility;
import sei.buaa.debug.utility.StringUtility;

public class Parser {

	private Set<Integer> faults = null;
	private int totalFailedTestCaseCnt = 0;
	private int totalPassedTestCaseCnt = 0;
	private int totalExecutableCodeCnt = 0;
	private double totalWeights = 0.0;
	private Map<Integer, StatementSum> map = new HashMap<Integer, StatementSum>();
	private boolean achieveCoincidentalCorrectness = false; //a flag indicates whether  CC info is obtained or not
	
	
	private int totalCC = 0;
	
	private StringBuilder sb_CC = new StringBuilder();
	private String programDir;
	String program = null;
	int versionID;
	
	public List<TestCase> addCoincidentalCorrectnessInfo(List<TestCase> list,
			String ccFilePath) {
		Set<Integer> set = new HashSet<Integer>();
		File file = new File(ccFilePath);
		if (file.exists()) {
			BufferedReader br = null;
			FileReader fr = null;
			String str;

			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				str = br.readLine();

				while (!StringUtility.IsNullOrEmpty(str)) {
					int testCaseId = Integer.parseInt(str);
					testCaseId++;
					set.add(testCaseId);

					str = br.readLine();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("CCFile doesn't exist!");
		}
		for (TestCase t : list) {
			if (set.contains(t.getId())) {
				t.setCoincidentalCorrectness(true);
			}
		}

		return list;
	}

	public Map<Integer, StatementSum> parser(String gcovDir) {

		Timer timer = new Timer();
		timer.start();

		File dir = new File(gcovDir);

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				gcovParser(file.getAbsolutePath());
			}
		} else {
			System.err.println(gcovDir + " is not a directory!");
		}

		if (achieveCoincidentalCorrectness)
		{
			
			String ccFileBasePath = gcovDir.substring(0,gcovDir.lastIndexOf("/")).replace("outputs", "coincidentalCorrectness");

			File ccBaseDir = new File(ccFileBasePath);
			if (ccBaseDir.exists() == false)
				ccBaseDir.mkdir();
			FileUtility.writeContentToFile(sb_CC.toString(), ccBaseDir+"/coincidentalCorrectness_"+StringUtility.getBaseName(gcovDir));
		}
		
		timer.end();
//		timer.timeElapse("analyzing gcov");

		return map;
	}

	private TestCase parseTestCase(String gcovFile)
	{
		String fileName = StringUtility.getBaseName(gcovFile);
		int passed = StringUtility.getDigit(fileName, fileName.length() - 1);
		int testCaseId = StringUtility.getDigit(fileName, fileName.length()-3);

		if (passed == 1)
			totalFailedTestCaseCnt++;
		else
			totalPassedTestCaseCnt++;

		TestCase testCase = new TestCase();
		testCase.setPassed(passed == 1 ? false : true);
		testCase.setId(testCaseId);

		BufferedReader br = null;
		FileReader fr = null;
		String str;
		totalExecutableCodeCnt = 0;

		try {
			fr = new FileReader(new File(gcovFile));
			br = new BufferedReader(fr);

			str = br.readLine();
			Statement st = null;
			while (str != null) {
				if (str.charAt(8) == '#') {
					str = str.replaceFirst("#####", "    0");
				}

				if (str.length() > 16 && str.charAt(15) == '-' && str.substring(16, 21).equals("block")) {
					st.setBlock(true);
				} else if (str.charAt(8) != '-' && str.charAt(9) == ':') {

					String[] strs = str.split(":");
					if (strs[2].trim().equals("{")) {
						str = br.readLine();
						continue;
					}
					totalExecutableCodeCnt++;
					int lineNumber = Integer.parseInt(strs[1].trim());
					int times = Integer.parseInt(strs[0].trim());

					st = new Statement(lineNumber, times);
					testCase.addStatement(st);
					if (times != 0)
						testCase.incrementExecutedStatements();
					
					addStatementSum(st, passed == 1 ? false : true);
					
				}
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
		
		return testCase;
	}
	
	private void gcovParser(String gcovFile) {

		TestCase testCase = parseTestCase(gcovFile);
		
		if (achieveCoincidentalCorrectness == true)
			achieveCoincidentalCorrectness(testCase);

		int totalBlockCnt = 0;
		int totalCoverBlockCnt = 0;
		
		for (Statement st: testCase.getStatements())
		{
			if (st.isBlock())
			{
				totalBlockCnt++;
				if (st.getCount() != 0)
					totalCoverBlockCnt++;
			}
		}
//		System.out.println("totalBlockCnt="+totalBlockCnt);
//		System.out.println("totalCoverBlockCnt="+totalCoverBlockCnt);
		
		if (testCase.isPassed() == false) {
			// System.out.println("totalELine="+totalELine);
			// System.out.println("totalExecutableCodeCnt="+totalExecutableCodeCnt);
			// System.out.println(wt);
//			double wt = Math.log(totalBlockCnt*1.0/totalCoverBlockCnt);
//			double wnf = Math.log(totalBlockCnt*1.0/(totalBlockCnt - totalCoverBlockCnt));
			
			double wt = totalBlockCnt*1.0/totalCoverBlockCnt;
			double wnf = totalBlockCnt*1.0/(totalBlockCnt - totalCoverBlockCnt+0.001);
			
			//weight proposed by Lee Naish
			double wtt = 1.0/(totalCoverBlockCnt-1+0.0001);
			totalWeights += wtt;
			
//			if (wt > 1.0)System.out.println(totalCoverBlockCnt+"/"+totalBlockCnt);
			for (Statement st : testCase.getStatements()) {
				
				StatementSum eSum = map.get(st.getLineNumber());
				if (st.getCount() != 0) {
					eSum.addToDA11(wt);
					eSum.addToDAef(wtt);					
				} else {					
					eSum.addToDA01(wnf);
				}
			}		
		} else {
			double wt = Math.log(totalBlockCnt) - Math.log(totalCoverBlockCnt);
			// System.out.println("totalELine="+totalELine);
			// System.out.println("totalExecutableCodeCnt="+totalExecutableCodeCnt);
			// System.out.println(wt);
			for (Statement st : testCase.getStatements()) {
				StatementSum eSum = map.get(st.getLineNumber());
				if (st.getCount() != 0) {
					eSum.addToDA10(wt);
				} else {
					double wnp = Math.log(totalBlockCnt)
							- Math.log(totalBlockCnt - totalCoverBlockCnt);
					eSum.addToDA00(wnp);
				}
			}
		}

	}
	
	public void arffGenerator(String programDir)
	{
		this.programDir = programDir;
		program = StringUtility.getBaseName(programDir);
		File dir = new File(programDir+"/outputs");
		
		File[] files = dir.listFiles();
		for (File file : files) {
			//System.out.println(versionID);
			versionID = Integer.valueOf(file.getName().substring(1));
			generator(file.getAbsolutePath());
			
		}
	}
	
	public void generator(String programDir)
	{		
		File dir = new File(programDir);

		List<TestCase> tcs = new ArrayList<TestCase>();
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				TestCase tc = parseTestCase(file.getAbsolutePath());
//				if (tc.isResult() == true)
					tcs.add(tc);
			}
		} else {
			System.err.println(programDir + " is not a directory!");
		}
		
		generateArff(tcs);
	}

	private void generateArff(List<TestCase> tcs) {
		StringBuilder builder = new StringBuilder();
		builder.append("@relation "+program+versionID+"\n");
		
		int attrCnt = totalExecutableCodeCnt;
		
		builder.append("@attribute ID integer\n");
		for (int i = 0; i < attrCnt; i++)
			builder.append("@attribute A"+i+" real\n");
		
		builder.append("@data\n");
		for (int i = 0; i < tcs.size(); i++)
		{
			builder.append(tcs.get(i).getId());
			for (int j = 0; j < tcs.get(i).getStatements().size(); j++)
			{
				builder.append(","+tcs.get(i).getStatements().get(j).getCount());
			}
			builder.append("\n");
		}
		FileUtility.writeContentToFile(builder.toString(),programDir+"/output_statement_arff/v"+versionID+".arff");
	}

	//if the testCase is CC, write this info into sb_CC
	private void achieveCoincidentalCorrectness(TestCase testCase)
	{
		if (testCase.isPassed() == true)
		{
			for (Statement s:testCase.getStatements())
			{
				if (s.isExecuted() && faults.contains(s.getLineNumber()))
				{
					totalCC++;
					sb_CC.append(testCase.getId()).append("\n");
					break;
				}
			}
		}
	}
	
	private void addStatementSum(Statement s, boolean testCaseResult) {
		
		
		StatementSum eSum = map.get(s.getLineNumber());
		if (eSum == null) {
			eSum = new StatementSum(s.getLineNumber());
			map.put(s.getLineNumber(), eSum);
		}
		if (s.isExecuted()) {
			if (testCaseResult)
				eSum.incrementA10();
			else
				eSum.incrementA11();
		}

	}

	
	
	public static void main(String[] args) {
		
		if (args.length == 0)
		{
			System.out.println("Invalid Usage!");
			System.out.println("Usage: ArffGenerator [programDir1 programDir2 ...]");
		}
		else 
		{
			for (String s: args)
			{
				System.out.println("Analyzing Program " + s);
				new Parser().arffGenerator(s);
			}
		}
		
//		Parser parser = new Parser();
//		parser.arffGenerator("/Users/csea/Documents/Experiment/Siemens/tot_info");
		//parser.faults = new HashSet<Integer>();
		//parser.faults.add(215);
		//parser.parser("/Users/csea/Documents/Experiment/Siemens/tot_info/outputs/v23");
		// parser.gcovParser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2/schedule.c.gcov_1797_0");
		// parser.gcovParser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2/schedule.c.gcov_2002_1");
	}

	public Map<Integer, StatementSum> getMap() {
		return map;
	}

	public int getTotalFailedTestCaseCnt() {
		return totalFailedTestCaseCnt;
	}

	public int getTotalPassedTestCaseCnt() {
		return totalPassedTestCaseCnt;
	}

	public int getTotalExecutableCodeCnt() {
		return totalExecutableCodeCnt;
	}

	public double getTotalWeights() {
		return totalWeights;
	}

	public void setFaults(Set<Integer> faults) {
		this.faults = faults;
	}

}
