package sei.buaa.debug.utility;

public class Constant {
	
	public static final String SOURCE_DIR = "/source.alt/source.orig/";
	public static final String VERSION_DIR  = "/versions.alt/versions.orig/";
	public static final String TEST_PLAN_DIR = "/testplans.alt/";
	public static final String OUT_PUT_DIR = "outputs";
	public static final String INPUT_DIR = "/inputs/";
	public static final String COINCIDENTAL_CORRECTNESS_DIR = "/coincidentalCorrectness/";
	/**
	 * list version id one per line for program variants to be analyzed
	 * */
	public static final String VERSIONS_LIST = "/versions.txt";
	/**
	 * presents fault locations for each faulty program.The line number indicates 
	 * program version id.The content of each line show fault locations information separated by space.
	 * 
	 * <p>For example, if the ith line contains the following string: 123 32. It tells us that
	 * the ith program variant contains two bug, which are located in 123th and 32th line respectively.
	 * An empty line indicates the corresponding program variant is absence of fault.
	 */
	public static final String FAULTS_LIST = "/faults.txt";
	
	public static final String TARANTULA = "Tarantula";
	public static final String JACCARD = "Jaccard";
	public static final String OCHIAI = "Ochiai";
	public static final String SBI = "SBI";
	public static final String WONG = "Wong";
	public static final String SIQ = "SIQ";
	public static final String RA1 = "RA1";

}
