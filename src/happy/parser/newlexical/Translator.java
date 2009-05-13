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
			System.out.println(tt);
		}
		return null;
		
	}
}
