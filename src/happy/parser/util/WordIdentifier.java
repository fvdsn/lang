package happy.parser.util;

import slip.parser.Cbinary;
import slip.parser.Ci;
import slip.parser.Clexpr;
import slip.parser.Cnull;
import slip.parser.Crexpr;
import slip.parser.Cvar;
import happy.parser.newlexical.LexicalTerm;

public class WordIdentifier {
	private static String[] RESERVED_WORD = {"fun", "method", "+", "-", "/", "*", "%", "set", 
		"write" , "read", "this", "super", "null", "if", "else", "while", "true", "false", "and",
		"or", "<", ">", "=", "<=", ">=", "!", "new", "return", "neg", "skip"};
	private static String[] BINARY_OP = {"or", "<", ">", "=", "<=", ">=", "and", "+", "-", "/", "*", "%" };
	
	private static String[] UNARY_OP = {"!", "neg" , "new"};
	
	private static String[] BINARY_ARI = { "+", "*", "-", "/", "%"};
	
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
	
	
	public static int getLevel(String methodId) {
		return Integer.parseInt(methodId.split("[.]")[1]);
	}
	
	public static boolean isRexpr(LexicalTerm term) {
		String t = term.getLexicalTerm();
		if(t.equals("number"))
			return true;
		if(t.equals("true"))
			return true;
		if(t.equals("null"))
			return true;
		if(t.equals("false"))
			return true;
			
		return false;
	}
	
	public static Crexpr getRexpr(LexicalTerm term) {
		String t = term.getLexicalTerm();
		if(t.equals("number"))
			return new Ci(Integer.parseInt(term.getValue())); 
		if(t.equals("true"))
			return new Ci(1);
		if(t.equals("null"))
			return new Cnull();
		if(t.equals("false"))
			return new Ci(0);
		return null;
	}
	
	
	public static boolean isBinaryArithmetic(LexicalTerm term) {
		for(String s : BINARY_ARI) {
			if(term.getValue().equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static Cbinary getBinary(LexicalTerm term, Cvar v1, Cvar v2) {
		for(int i = 0; i < BINARY_ARI.length; i++) {
			if(term.getValue().equals(BINARY_ARI[i])) {
				return new Cbinary(v1, i, v2);
			}
		}
		
		return null;
	}
}
