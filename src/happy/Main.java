package happy;


import java.util.Hashtable;

import happy.parser.bnf.BnfParser;

import happy.parser.bnf.Term;

import happy.parser.lexical.ParserIterator;
import happy.parser.syntax.SyntaxParser;
import happy.checker.checkWP;

public class Main {
	private ParserIterator lp = null;
	private SyntaxParser sp  = null; 
	private BnfParser bnfp   = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = null;
	private boolean wpvalid = false;
	
	public Main(String bnfSyntax) {
		
		try {
			bnfp = new BnfParser(bnfSyntax);
			
		} catch (Exception e) {
				e.printStackTrace();
		}
		//prectable = CheckPrecedence.createTable(bnfp.getRules());
		//wpvalid = CheckPrecedence.precTable(bnfp.getRules(),prectable );
	}
	public void interpret(String codefile){
		try{
			lp = new ParserIterator(codefile);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(wpvalid){
			sp = new SyntaxParser(lp,bnfp.getRules(),prectable);
			sp.parse();
		}else{
			System.out.println("Syntax is not WP");
		}
	}
	public void checkGrammar(){
		checkWP.checkAll(bnfp.getRules());
	}
	public static void main(String args[]){
		Main m = new Main("temp.bnf");
		m.checkGrammar();
		//m.interpret("test1");
	}
}
