package happy.parser.newlexical;

import java.io.IOException;
import java.util.List;

import slip.parser.Cmethod;

import happy.parser.bnf.BnfParser;
import happy.parser.bnf.Rule;

public class Translator {
	public Translator(LexicalTerm tree) {
		for(LexicalTerm t : tree.getLexChildList()) {
			if(!t.isTerminal()) {
				analyseMethod(t);
			}
		}
	}
	
	public Cmethod analyseMethod(LexicalTerm t) {
		List<LexicalTerm> child = t.getLexChildList();
		child.remove(0);
		child.remove(child.size() - 1);
		for(LexicalTerm tt : t.getLexChildList()) {
			if(tt.lexicalTerm == null) {
				System.out.println("list");
			}
			else if(tt.lexicalTerm.equals("fun")) {
				System.out.println("fun");
			}
			else if(tt.lexicalTerm.equals("method_int")) {
				
				System.out.println("method");
			}
			else {
				System.out.println("Erreur de syntaxe");
				System.exit(0);
			}
			
		}
		return null;
		
	}
}
