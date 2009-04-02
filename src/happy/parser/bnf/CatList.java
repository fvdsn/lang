package happy.parser.bnf;

import java.util.ArrayList;
import java.util.List;

public class CatList implements Term {
	List<Term> list;
	
	public CatList() {
		list = new ArrayList<Term>();
	}
	
	@Override
	public boolean isTerminal() {
		return false;
	}
	
	public void add(Term t) {
		list.add(t);
	}
	
	public List<Term> getTermList() {
		return list;
	}
	public Term getFirst(){
		/*TODO*/
		return null;
	}
	public Term getLast(){
		/*TODO*/
		return null;
	}
	
	public String toString() {
		
		StringBuilder res = new StringBuilder("");
		boolean first = true;
		for(Term t : list) {
			if(first) {
				res.append(t + ":" + t.isTerminal());		
				first = false;
			}
			else 
				res.append(" " + t + ":" + t.isTerminal());
		}
		return res.toString();
	}
	
	

}
