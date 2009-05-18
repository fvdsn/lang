package happy.parser.bnf;

import java.util.LinkedList;
import java.util.List;

public class TermImpl implements Term {
	String term;
	protected String value;
	boolean terminal;
	int	line;
	int collumn;
	List <Term> childList;
	
	public TermImpl(String term, boolean terminal) {
		this.term = term;
		this.terminal = terminal;
		if(terminal) 
			this.value = term;
		else 
			this.value = null;
		this.childList = new LinkedList<Term>();
		this.line = 0;
		this.collumn = 0;
	}
	
	@Override
	public boolean isTerminal() {
		// TODO Auto-generated method stub
		return terminal;
	}
	public String getType(){
		return term;
	}
	
	@Override
	public String toString() {
		return term;
	}
	public String getValue(){
		return value;
	}
	public Term setValue(String v){
		this.value = v;
		return this;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (terminal + term).hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TermImpl))
			return false;
		TermImpl other = (TermImpl) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		if (terminal != other.terminal)
			return false;
		return true;
	}

	@Override
	public List<Term> getChildList() {
		return childList;
	}

	@Override
	public int getCollumn() {
		return collumn;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public void setCollumn(int col) {
		collumn = col;
	}
	private void indent(int level){
		while(level-- >= 0){
			System.out.print(":  ");
		}
	}
	public void printTree(int indent){
		
		indent(indent);
		
		if(value != null){
			System.out.println(value+" ("+term+") "+line);
		}else{
			System.out.println("<"+term+">");
		}
		for(Term t :childList){
			t.printTree(indent + 1);
		}
	}

	@Override
	public void setLine(int line) {
		this.line = line;
	}
}
