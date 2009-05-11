package happy.parser.newlexical;

import happy.parser.bnf.TermImpl;

public class LexicalTerm extends TermImpl {
	String lexicalTerm;
	
	public LexicalTerm(String term, boolean terminal, String lexicalTerm, String value) {
		super(term, terminal);
		this.lexicalTerm = lexicalTerm;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equal = super.equals(obj);
		if(equal) {
			if(obj instanceof LexicalTerm) {
				LexicalTerm t = (LexicalTerm) obj;
				return t.lexicalTerm.equals(this.lexicalTerm) && t.value.equals(this.value);
			}
		}
		return false;
	}
}
