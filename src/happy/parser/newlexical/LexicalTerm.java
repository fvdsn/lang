package happy.parser.newlexical;

import java.util.LinkedList;
import java.util.List;

import happy.parser.bnf.Term;
import happy.parser.bnf.TermImpl;

public class LexicalTerm extends TermImpl {
	String lexicalTerm;
	List<LexicalTerm> child = new LinkedList<LexicalTerm>();
	
	public LexicalTerm(String term, String lexicalTerm, String value) {
		super(term, true);
		this.lexicalTerm = lexicalTerm;
		this.value = value;
	}
	
	public LexicalTerm(String term, List<Term> child) {
		super(term, false);
		for(Term t : child) {
			if(t.isTerminal()) {
				LexicalTerm tt = (LexicalTerm) t;
				this.child.add(tt);
			}
			else {
				this.child.add(new LexicalTerm(t.getType(), t.getChildList()));
			}
		}
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
	
	public static final String BINARY_OP = "binary_op";
	public static final String UNARY_OP = "unary_op";
	
	
	
	public List<LexicalTerm> getLexChildList() {
		
		return child;
	}
	
	private void indent(int level){
		while(level-- >= 0){
			System.out.print(":  ");
		}
	}
	public void printLexTree(int indent){
		
		indent(indent);
		
		if(value != null){
			System.out.println(value+" ("+lexicalTerm+") ");
		}else{
			System.out.println("<"+getType()+">");
			
		}
		for(LexicalTerm t :child){
			
			t.printLexTree(indent + 1);
		}
	}
	
	/**
	 * @deprecated
	 */
	public List<Term> getChildList() {
		// TODO Auto-generated method stub
		return super.getChildList();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "=> " + value + " => " + lexicalTerm;
	}
	
	public String getLexicalTerm() {
		return lexicalTerm;
	}
}
