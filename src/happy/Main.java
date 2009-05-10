package happy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import happy.checker.CheckPrecedence;
import happy.parser.bnf.BnfParser;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;
import happy.parser.lexical.CodeLoader;
import happy.parser.lexical.LexicalParser;
import happy.parser.lexical.ParserIterator;
import happy.parser.syntax.SyntaxParser;
public class Main {
	private ParserIterator lp = null;
	private SyntaxParser sp  = null; 
	private BnfParser bnfp   = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = null;
	private boolean wpvalid = false;
	
	public Main(String bnfSyntax){
		List<Term> allTerm = null;
		try {
			bnfp = new BnfParser(bnfSyntax);
		} catch (Exception e) {
				e.printStackTrace();
		}
		prectable = CheckPrecedence.createTable(bnfp.getRules());
		wpvalid = CheckPrecedence.precTable(bnfp.getRules(),prectable );
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
	public static void main(String args[]){
		Main m = new Main("grammar_wp_space.bnf");
		m.interpret("test1");
	}
}
