package sei.buaa.debug.core;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sei.buaa.debug.entity.Statement;
import sei.buaa.debug.entity.TestCase;
import sei.buaa.debug.utility.StringUtility;


public class Parser {
	
	public List<TestCase> addCoincidentalCorrectnessInfo(List<TestCase> list,String ccFilePath)
	{
		Set<Integer> set = new HashSet<Integer>();
		File file = new File(ccFilePath);
		if (file.exists())
		{
			BufferedReader br = null;
			FileReader fr = null;
			String str;		
			
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				
				str = br.readLine();
				
				while (!StringUtility.IsNullOrEmpty(str))
				{
					int testCaseId = Integer.parseInt(str);
					testCaseId++;
					set.add(testCaseId);
					//System.out.println(ccFilePath+":"+testCaseId);
					
					str = br.readLine();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		else 
		{
			System.out.println("CCFile doesn't exist!");
		}
		
		for (TestCase t:list)
		{
			if (set.contains(t.getId()))
			{
				t.setCoincidentalCorrectness(true);
			}
		}
		
		return list;
	}
	
	public List<TestCase> parser(String gcovDir)
	{
		List<TestCase> ret = new ArrayList<TestCase>();
		
		File dir = new File(gcovDir);
		
		if (dir.isDirectory())
		{
			File[] files = dir.listFiles();
			for (File file: files)
			{
				TestCase tc = gcovParser(file.getAbsolutePath());
				ret.add(tc);
			}
		}
		else{
			System.err.println(gcovDir+" is not a directory!");
		}
		
		return ret;
	}
	
	private TestCase gcovParser(String gcovFile)
	{
		TestCase tc = new TestCase();
		
		String fileName = StringUtility.getBaseName(gcovFile);
		int passed = StringUtility.getDigit(fileName, fileName.length()-1);
		tc.setPassed(passed == 1 ? false : true);
		tc.setId(StringUtility.getDigit(fileName, fileName.length()-3));
		
		BufferedReader br = null;
		FileReader fr = null;
		String str;		
		try {
			fr = new FileReader(gcovFile);			
			br = new BufferedReader(fr);
			
			str = br.readLine();			
			while (str != null)
			{
				if (str.charAt(8) == '#')
				{
					str = str.replaceFirst("#####", "    0");
				}
								
				if (str.charAt(8) != '-' && str.charAt(9) == ':')
				{	
					String[] strs = str.split(":");
					if (strs[2].trim().equals("{"))
					{
						str = br.readLine();
						continue;
					}
					int lineNumber = Integer.parseInt(strs[1].trim());
					int times = Integer.parseInt(strs[0].trim());
					
					Statement st = new Statement(lineNumber,times);
					
					if (times != 0)
						tc.incrementExecutedStatements();
					
					tc.addStatement(st);					
				}
				// read another line
				str = br.readLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			//System.out.println(tc);
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return tc;
	}
	
	public static void main(String[] args)
	{
		Parser parser = new Parser();
		parser.parser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2");
		//parser.gcovParser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2/schedule.c.gcov_1797_0");
		//parser.gcovParser("/Users/csea/Documents/Experiment/Siemens/schedule/outputs/v2/schedule.c.gcov_2002_1");
	}

}
