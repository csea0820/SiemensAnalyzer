package sei.buaa.debug.core;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sei.buaa.debug.utility.Constant;
import sei.buaa.debug.utility.FileUtility;
import sei.buaa.debug.utility.StringUtility;

public class ScriptGenerator {

	StringBuilder sb = null;

	String programName;
	
	String programDir;
	String testPlanFile;
	List<Integer> versions = null;

	public void runScriptGen(String programDir, String testPlanFile) {
		sb = new StringBuilder();
		versions = new ArrayList<Integer>();

		this.programDir = programDir;
		if (StringUtility.IsNullOrEmpty(testPlanFile))
			this.testPlanFile = "universe";
		else
			this.testPlanFile = testPlanFile;

		programName = StringUtility.getBaseName(programDir);
		System.out.println("Program Name :" + programName);
		getVersionsInfo(programDir + "/versions.txt");

		globalVarGen();
		functionDefGen();
		programBodyGen();

		FileUtility.writeContentToFile(sb.toString(),
				programDir+"/testCase_"+programName+".sh");
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

	private void globalVarGen() {
		sb.append("#!/bin/sh\n");
		sb.append("#\n");
		sb.append("#    Global Variables For Script\n");
		sb.append("#\n");
		sb.append("programDir=").append(programDir).append("\n");
		sb.append("testPlan=").append(testPlanFile).append("\n");
		sb.append("programName=`basename $programDir`\n");
		sb.append("sourceDir=\"").append(Constant.SOURCE_DIR).append("\"\n");
		sb.append("versionDir=\"").append(Constant.VERSION_DIR).append("\"\n");
		sb.append("testPlanDir=\"").append(Constant.TEST_PLAN_DIR)
				.append("\"\n");
		sb.append("outDir=\"").append(Constant.OUT_PUT_DIR).append("\"\n");
	}

	private void functionDefGen() {
		sb.append("#\n").append("#\n")
				.append("#    Function Definition For Script\n").append("#\n")
				.append("#\n");

		// help function
		sb.append("help()\n");
		sb.append("{\n");
		sb.append("cat << HELP\n");
		sb.append("    Usage: run.sh programDir [testPlanPath]\n");
		sb.append("    Example: run.sh /User/csea/Documents/Experiment/Siemens/tcas universe\n");
		sb.append("HELP\n");
		sb.append("exit 0\n");
		sb.append("}\n\n");

		// moveGcovToOutputDir
		sb.append("moveGcovToOutput()\n");
		sb.append("{\n");
		sb.append("    mv $1.c.gcov ${outDir}/v${2}/$1.c.gcov_$3\n");
		sb.append("}\n\n");

		// createDir
		sb.append("createDir()\n{\n");
		sb.append("\tif [ -d \"$1\" ];then\n");
		sb.append("\t\trm -rfd $1\n\tfi\n");
		sb.append("\tmkdir $1\n}\n\n");
	}

	private void programBodyGen() {
		sb.append("#\n#  Program Body For Script\n#\n");
		sb.append("\tif [ -n \"$testPlan\" ];then\n");
		sb.append("\t\tif [ ! -f \"$programDir$testPlanDir$testPlan\" ];then\n");
		sb.append("\t\t\thelp\n");
		sb.append("\t\tfi\n");
		sb.append("\telse\n");
		sb.append("\t\t\ttestPlan=\"universe\"\n");
		sb.append("\tfi\n\n");

		sb.append("\techo \"Program $programName is under analyzing with testplan $testPlan\"\n");
		sb.append("\tcd $programDir\n");
		sb.append("\tgcc $programDir$sourceDir$programName.c -o $programName.exe\n\n");

		for (int versionId : versions) {

			sb.append("\t\tversion=").append(versionId).append("\n");
			sb.append("\t\techo \"Analyzing version_$version\"\n");
			sb.append("\t\tcreateDir ${outDir}/v$version\n");
			sb.append("\t\tcp $programDir${versionDir}v${version}/* .\n");
			sb.append("\t\tgcc -fprofile-arcs -ftest-coverage -o ${programName}_${version}.exe $programName.c\n\n");

			sb.append("\t\ttouch originalResult.txt\n");
			sb.append("\t\ttouch currentResult.txt\n\n");
			sb.append("\t\ti=1\n");

			// generator test case execution script
			testCaseGen(programDir + Constant.TEST_PLAN_DIR + testPlanFile,versionId);

			sb.append("\t\trm originalResult.txt\n");
			sb.append("\t\trm currentResult.txt\n");
			sb.append("\t\trm ${programName}_${version}.exe\n");
			sb.append("\t\trm $programName.gcno\n");
			sb.append("\t\trm *.c\n");
			sb.append("\t\trm *.h\n");
		}
		sb.append("\trm $programName.exe\n");
	}

	private void testCaseGen(String testPlanPath,int versionId) {
		BufferedReader bufferReader = null;
		String str = null;
		String testCase = null;
		 int i = 0;
		try {
			bufferReader = new BufferedReader(new FileReader(testPlanPath));

			str = bufferReader.readLine();
	
			while (str != null) {
				// if (i++ > 10)break;
				testCase = convert(str);
				testCaseExecution(testCase,versionId,++i);
				str = bufferReader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			bufferReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void testCaseExecution(String testCase,int versionId,int testCaseId) {
		sb.append("\t\techo \"${programName}_${version} >>>>>>>>>>>>>>>>>>>>> test case ")
				.append(testCaseId).append("\"\n");
		
		sb.append("\t\t./").append(programName).append(".exe ").append(testCase).append(" > originalResult.txt\n");
		sb.append("\t\t./").append(programName).append("_").append(String.valueOf(versionId)).append(".exe ").append(testCase).append(" > currentResult.txt\n");
		
		/*
		sb.append("\t\t./$programName.exe ").append(testCase)
				.append(" > originalResult.txt\n");
		sb.append("\t\t./${programName}_${version}.exe ").append(testCase)
				.append(" > currentResult.txt\n");
		*/
		
		
		sb.append("\t\tgcov -a $programName.c\n");

		sb.append("\t\tdiff originalResult.txt currentResult.txt\n");
		sb.append("\t\toracle=$?\n");
		sb.append("\t\tmoveGcovToOutput $programName $version ${i}_${oracle}\n");
		sb.append("\t\t((i++))\n");
		sb.append("\t\trm $programName.gcda\n\n");
	}

	private String convert(String s) {
		if (s == null)
			return s;
		if (s.startsWith(".."))
			s = s.substring(1);
		String[] temp = s.split("<");
		String result = new String();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].length() == 0)
				continue;

			if (s.indexOf("<" + temp[i]) != -1) {
				temp[i] = " ." + Constant.INPUT_DIR + temp[i].trim();
				result += "<" + temp[i];
			} else
				result += temp[i];
		}
		return result;
	}

	public static void main(String args[]) {
		
		if (args.length == 0)
		{
			System.out.println("Invalid Usage!");
			System.out.println("Usage: ScriptGenerator [programDir1 programDir2 ...]");
		}
		else 
		{
			for (String s: args)
			{
				new ScriptGenerator().runScriptGen(s, null);
			}
		}
	}

}
