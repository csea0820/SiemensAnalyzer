package sei.buaa.debug.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sei.buaa.debug.entity.Expensive;
import sei.buaa.debug.entity.JaccardSusp;
import sei.buaa.debug.entity.OchiaiSusp;
import sei.buaa.debug.entity.Statement;
import sei.buaa.debug.entity.StatementSum;
import sei.buaa.debug.entity.Suspiciousness;
import sei.buaa.debug.entity.TarantulaSusp;
import sei.buaa.debug.entity.TestCase;
import sei.buaa.debug.entity.Version;
import sei.buaa.debug.utility.Constant;
import sei.buaa.debug.utility.StringUtility;

public class ProjectAnalyzer {
		
	Map<Integer,List<Integer> > faults = null;
	List<Integer> versions = null;
	String programName;
	String programDir;
	
	private int totalVersions;
	private int availableVersions;
	
	SiemensAnalyzer sa;
	
	public ProjectAnalyzer(SiemensAnalyzer sa,String programDir)
	{
		this.programDir = programDir;
		this.sa = sa;
		totalVersions = 0;
		availableVersions = 0;
		programName = StringUtility.getBaseName(programDir);
		faults = new HashMap<Integer,List<Integer> >();
		versions = new ArrayList<Integer>();
		init();
	}
	
	private void init()
	{
		getVersionsInfo(programDir+"/versions.txt");
		getFaultLocation(programDir+"/faults.txt");
	}
	
	public void analyze()
	{
		Parser parser = new Parser();
		for (int vid:versions)
		{
			System.out.println("analyzing program " + programName + ",verions " + vid);
			Version v = new Version();
			v.setName(programName);
			v.setVersionId(vid);
			v.addFaults(faults);
			totalVersions++;
			if (v.getFaultNumber() != 1)continue;
			
			List<TestCase> list = parser.parser(programDir+"/"+Constant.OUT_PUT_DIR+"/v"+vid);
			v.setTotalExecutableCode(list.get(0).getStatements().size());
			analyzeVersion(v, list);
		}
	}
	
	public void analyzeVersion(Version v,List<TestCase> list)
	{
		Map<Integer,StatementSum> map = new HashMap<Integer,StatementSum>();
		for (TestCase tc: list)
		{
			boolean isPassed = tc.isPassed();
			if (isPassed)
				v.passedIncrement();
			else v.failedIncrement();
			
			for (Statement s: tc.getStatements())
			{
				if (map.containsKey(s.getLineNumber()))
				{
					StatementSum eSum = map.get(s.getLineNumber());
					if (s.isExecutedOrNot() == 1)
					{
						if (isPassed)
							eSum.incrementA10();
						else eSum.incrementA11();
					}
				}
				else
				{
					StatementSum eSum = new StatementSum(s.getLineNumber());
					map.put(s.getLineNumber(), eSum);
				}
			}
		}

		if (v.getTotalFailedCount() == 0)return ;
		availableVersions++;
		
		//System.out.println(v);
		List<Suspiciousness> tarantulaSusps = new ArrayList<Suspiciousness>();
		List<Suspiciousness> jaccardSusps = new ArrayList<Suspiciousness>();
		List<Suspiciousness> ochiaiSusps = new ArrayList<Suspiciousness>();
		for (StatementSum eSum : map.values())
		{
			eSum.setA00(v.getTotalPassedCount()-eSum.getA10());
			eSum.setA01(v.getTotalFailedCount()-eSum.getA11());
			
			TarantulaSusp s = new TarantulaSusp(eSum.getLineNumber());
			s.calcSups(eSum.getA00(), eSum.getA01(), eSum.getA10(), eSum.getA11());
			tarantulaSusps.add(s);
			
			JaccardSusp js = new JaccardSusp(eSum.getLineNumber());
			js.calcSups(eSum.getA00(), eSum.getA01(), eSum.getA10(), eSum.getA11());
			jaccardSusps.add(js);
			
			OchiaiSusp os = new OchiaiSusp(eSum.getLineNumber());
			os.calcSups(eSum.getA00(), eSum.getA01(), eSum.getA10(), eSum.getA11());
			ochiaiSusps.add(os);
		}
		
		rank(v,tarantulaSusps,TarantulaSusp.class.getSimpleName(),sa.getTarantulaExp());
		rank(v,jaccardSusps,JaccardSusp.class.getSimpleName(),sa.getJaccardExp());
		rank(v,ochiaiSusps,OchiaiSusp.class.getSimpleName(),sa.getOchiaiExp());
	}
	
	private void rank(Version v,List<Suspiciousness> susp,String fl,Expensive exp)
	{
		Collections.sort(susp);
		v.calcExamineEffort(susp);
		v.writeResultToFile(susp, programDir+"/FL",fl);
		exp.addExpensive(v.getExpensive());
	}
	
	public void getFaultLocation(String faultFile)
	{
		BufferedReader br = null;
		FileReader fr = null;
		String str;		
		try {
			fr = new FileReader(faultFile);			
			br = new BufferedReader(fr);
			
			str = br.readLine();			
			while (str != null)
			{
				String[] nums = str.split("\\s+");
				int versionId = Integer.parseInt(nums[0]);
				List<Integer> fault = new ArrayList<Integer>();
				for (int i = 1; i < nums.length; i++)
				{
					fault.add(Integer.parseInt(nums[i]));
				}
				
				faults.put(versionId, fault);
				// read another line
				str = br.readLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
//		for (Integer key : faults.keySet())
//		{
//			for (Integer value : faults.get(key))
//			{
//				System.out.println("version="+key+",fault="+value);
//			}
//		}
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
	
	
	
	public static void main(String[] args)
	{
	}

	public int getTotalVersions() {
		return totalVersions;
	}

	public int getAvailableVersions() {
		return availableVersions;
	}

}
