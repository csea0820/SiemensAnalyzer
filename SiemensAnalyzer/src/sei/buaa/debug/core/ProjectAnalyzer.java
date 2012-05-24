package sei.buaa.debug.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sei.buaa.debug.entity.JaccardSusp;
import sei.buaa.debug.entity.OchiaiSusp;
import sei.buaa.debug.entity.Statement;
import sei.buaa.debug.entity.StatementSum;
import sei.buaa.debug.entity.Suspiciousness;
import sei.buaa.debug.entity.TarantulaSusp;
import sei.buaa.debug.entity.TestCase;
import sei.buaa.debug.entity.Version;

public class ProjectAnalyzer {
		
	Parser parser = new Parser();
	
	public void analyzerVersion(List<TestCase> list)
	{
		Map<Integer,StatementSum> map = new HashMap<Integer,StatementSum>();
		Version v = new Version();
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
		
		System.out.println(v);
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
			
			System.out.println("--------------");
			System.out.println(eSum);
			System.out.println(s);
			System.out.println(os);
		}
		
//		System.out.println("--------Tarantula----------");
//		rank(tarantulaSusps);
//		System.out.println("--------Jaccard----------");
//		rank(jaccardSusps);
//		System.out.println("--------Ochiai----------");
		rank(ochiaiSusps);
	}
	
	private void rank(List<Suspiciousness> susp)
	{
		Collections.sort(susp);
		for (Suspiciousness s : susp)
		{
			System.out.println(s);
		}
	}
	
	public static void main(String[] args)
	{
		Parser parser = new Parser();
		List<TestCase> list = parser.parser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2");
		ProjectAnalyzer pa = new ProjectAnalyzer();
		pa.analyzerVersion(list);
		//parser.gcovParser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2/schedule.c.gcov_1797_0");
		//parser.gcovParser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2/schedule.c.gcov_2002_1");
	}

}
