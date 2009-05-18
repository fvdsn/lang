package happy.parser.bnf;

import java.util.List;

public interface Term {
	/**
	 * 
	 * @return true if the term is a terminal term
	 */
	public boolean isTerminal();
	public String getType();
	public String getValue();
	public Term setValue(String v);
	int	   getLine();
	void   setLine(int line);
	int		getCollumn();
	void	setCollumn(int col);
	public List<Term> getChildList();
	void	printTree(int indent);
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object obj);
	
}
