package happy.parser.util;

public class CharIdentifier {
	public static char[] DIGIT = {'0', '1', '2', '3', '4' , '5' , '6' , '7', '8', '9'};

	public static boolean isSpace(char c) {
		if(c == ' ')
			return true;
		if(c == '\n')
			return true;
		if(c == '\t')
			return true;
		
		return false;
	}
	
	public static int isReserved(char c) {
		if(c == '(') {
			return (int) '(';
		}
		if(c == ')') {
			return (int) ')';
		}
		return -1;
	}
	
	public static boolean isDigit(char c) {
		for(char d : DIGIT) {
			if(c == d) {
				return true;
			}
		}
		return false;
	}
	
	
}
