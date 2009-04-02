package happy.parser.util;

public class CharIdentifier {

	public static boolean isSpace(char c) {
		if(c == ' ')
			return true;
		if(c == '\n')
			return true;
		if(c == '\t')
			return true;
		
		return false;
	}
	
	public static boolean isSuffix(String a, String b) {
		int lenA = a.length();
		int lenB = b.length();
		if(lenA >= lenB) {
			return a.endsWith(b);
		}
		else {
			return b.endsWith(a);
		}
		
	}
}
