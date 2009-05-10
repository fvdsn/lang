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
import happy.parser.syntax.SyntaxParser;
public class Main {
	private LexicalParser lp = null;
	private SyntaxParser sp  = null; 
	private BnfParser bnfp   = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = 
		new Hashtable<Term,Hashtable<Term,String>>();
	private boolean wpvalid = false;
	
	public Main(String bnfSyntax){
		List<Term> allTerm = null;
		try {
			bnfp = new BnfParser(bnfSyntax);
		} catch (Exception e) {
				e.printStackTrace();
		}
		allTerm = CheckPrecedence.getAllTerm(bnfp.getRules());
		for(Term l: allTerm){
			Hashtable<Term,String> line = new Hashtable<Term,String>();
			prectable.put(l,line);
			for(Term col:allTerm){
				line.put(col, CheckPrecedence.NOTHING);
			}
		}
		wpvalid = CheckPrecedence.precTable(bnfp.getRules(),prectable );
	}
	public void interpret(String codefile){
		CodeLoader cl = null;
		try{
			cl = new CodeLoader(codefile);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(wpvalid){
			lp = new LexicalParser(cl.getRep());
			sp = new SyntaxParser(lp,bnfp.getRules(),prectable);
			sp.parse();
		}else{
			System.out.println("Syntax is not WP");
		}
	}
	public static void main(String args[]){
		Main m = new Main("grammar_wp.bnf");
		m.interpret("test1");
	}
}
