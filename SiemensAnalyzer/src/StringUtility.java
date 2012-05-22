
public class StringUtility {
	
	public static boolean IsNullOrEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}

	public static String getBaseName(String path)
	{
		int index = path.lastIndexOf("/");
		return path.substring(index+1);
	}
}
