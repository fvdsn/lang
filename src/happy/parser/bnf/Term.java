package happy.parser.bnf;

public interface Term {
	/**
	 * 
	 * @return true if the term is a terminal term
	 */
	public boolean isTerminal();
	
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object obj);
	
}
