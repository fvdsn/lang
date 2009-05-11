package happy.parser.util;

public class WordIdentifier {
	private static String[] RESERVED_WORD = {"fun", "method", "+", "-", "/", "*", "%", "set", 
		"write" , "read", "this", "super", "null", "if", "else", "while", "true", "false", "and",
		"or", "<", ">", "=", "<=", ">=", "!", "new", "return", "neg", "skip"};
	private static String[] BINARY_OP = {"or", "<", ">", "=", "<=", ">=", "and", "+", "-", "/", "*", "%" };
	
	private static String[] UNARY_OP = {"!", "neg" , "new"};
	
	private static String[] FORBID_DOT_EXPR = {"fun", "+", "-", "/", "*", "%", "set", 
		"write" , "read", "null", "if", "else", "while", "true", "false", "and",
		"or", "<", ">", "=", "<=", ">=", "not"};
	
	public static String isReservedWord(String word) {
		for(String s : RESERVED_WORD) {
			if(s.equals(word)) {
				return s;
			}
		}
		return null;
	}
	
	public static boolean isBinaryOp(String word) {
		for(String s : BINARY_OP) {
			if(s.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUnaryOP(String word) {
		for(String s : UNARY_OP) {
			if(s.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Définit le type d'expression 
	 * @param before
	 * @param after
	 * @return
	 */
	public static String getDotExpr(String before, String after) {
		for(String s : FORBID_DOT_EXPR) {
			if(s.equals(before)) {
				System.out.println("Erreur de syntaxe at " + before + "." + after);
				System.exit(-1);
			}
		}
		boolean reserved = false;
		for(String s : RESERVED_WORD) {
			if(s.equals(before))
				reserved = true;
			if(s.equals(after)) {
				System.out.println("Erreur de syntaxe at " + before + "." + after);
				System.exit(-1);
			}
		}
		if(!reserved) {
			before = "id";
		}
		
		try {
			int i = Integer.parseInt(after);
			if(i < 0) {
				System.out.println("Erreur de syntaxe at " + before + "." + after);
				System.exit(-1);
			}
			after = "int";
			if(before.equals("super")) {
				System.out.println("Erreur de syntaxe at " + before + "." + after);
				System.exit(-1);
			}
			
			
		}
		catch(NumberFormatException e) {
			after = "id";
			if(before.equals("method")) {
				System.out.println("Erreur de syntaxe at" + before + "." + after);
				System.exit(-1);
			}
		}
		
		return before + "_" + after; 
	}
	
	
	
}
