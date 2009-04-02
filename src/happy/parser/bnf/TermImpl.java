package happy.parser.bnf;

public class TermImpl implements Term {
	String term;
	String token;
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


}
