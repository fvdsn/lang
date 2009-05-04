package happy.parser.bnf;

import java.util.List;

public interface Term {
	/**
	 * 
	 * @return true if the term is a terminal term
	 */
	public boolean isTerminal();
	public String getValue();
	public Term setValue(String v);
	public List<Term> getChildList();
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object obj);
	
}
