package happy;


import java.util.Hashtable;

import happy.parser.bnf.BnfParser;

import happy.parser.bnf.Term;


import happy.parser.newlexical.LexicalParser;
import happy.parser.syntax.SyntaxParser;
import happy.parser.syntax.SyntaxParserNextGen;
import happy.parser.syntax.Translator;
import happy.parser.syntax.TreeOrganiser;
import happy.checker.CheckPrecedence;
import happy.checker.checkWP;

public class Main {
	private LexicalParser lp = null;
	private SyntaxParser sp  = null;
	private SyntaxParserNextGen spn  = null; 
	private BnfParser bnfp   = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = null;
	
	
	public Main(String bnfSyntax) {
		
		try {
			bnfp = new BnfParser(bnfSyntax);
			
		} catch (Exception e) {
				e.printStackTrace();
		}
		
		prectable = CheckPrecedence.createTable(bnfp.getRules());
		CheckPrecedence.precTable(bnfp.getRules(),prectable );
	}
	public void interpret(String codefile){
		try{
			lp = new LexicalParser(codefile);
		}catch(Exception e){
			e.printStackTrace();
		}
		
			sp = new SyntaxParser(lp,bnfp.getRules(),prectable);
			sp.parse();
		
	}
	public void interpret2(String codefile){
		try{
			lp = new LexicalParser(codefile);
		}catch(Exception e){
			e.printStackTrace();
		}
		spn = new SyntaxParserNextGen(lp,bnfp.getRules(),prectable);
		spn.parse();
		if(spn.getParsedTree() != null){
			TreeOrganiser to = new TreeOrganiser(spn.getParsedTree());
			new Translator(to.contract());
		}
	}
	public boolean checkGrammar(){
		return checkWP.checkAll(bnfp.getRules());
	}
	public static void main(String args[]){
		//System.out.println(args[0]+"  "+args[1]);
		Main m = new Main(args[0]);
		if(args.length >= 3 && args[2].equals("check")){
			if(m.checkGrammar()) {
				m.interpret(args[1]);
			}
			else {
				System.out.println("Grammaire non wp");
			}
		}else{
			m.interpret2(args[1]);
		}

		
	}
}
