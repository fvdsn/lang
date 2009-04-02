package happy.parser.bnf;

public class TermImpl implements Term {
	String term;
	boolean terminal;
	
	public TermImpl(String term, boolean terminal) {
		this.term = term;
		this.terminal = terminal;
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
	
	


}
