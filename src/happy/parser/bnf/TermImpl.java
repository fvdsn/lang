package happy.parser.bnf;

import java.util.LinkedList;
import java.util.List;

public class TermImpl implements Term {
	String term;
	String value;
	boolean terminal;
	List <Term> childList;
	
	public TermImpl(String term, boolean terminal) {
		this.term = term;
		this.terminal = terminal;
		this.value = term;
		this.childList = new LinkedList();
	}
	
	@Override
	public boolean isTerminal() {
		// TODO Auto-generated method stub
		return terminal;
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
}
