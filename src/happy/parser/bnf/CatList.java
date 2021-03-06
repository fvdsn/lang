package happy.parser.bnf;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * this class provide a wrapper for the right side of a rule between or bnf symbole |
 * This list contains Terms 
 *
 */
public class CatList {
	List<Term> list;
	
	public CatList() {
		list = new ArrayList<Term>();
	}
	
	public boolean isTerminal() {
		return false;
	}
	
	public void add(Term t) {
		list.add(t);
	}
	public boolean match(CatList other){
		return this.stringRep().equals(other.stringRep());
	}
	
	public List<Term> getTermList() {
		return list;
	}
	
	public Term getFirst(){
		return list.get(0);
	}
	
	public Term getLast(){
		return list.get(list.size() - 1);
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
	
	public String stringRep() {
		StringBuilder res = new StringBuilder("");
		for(Term t : list) {
			res.append(t);		
		}
		return res.toString();
	}
	
	

	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) 
			return false;
		if (obj instanceof CatList) {
			CatList other = (CatList) obj;
			return stringRep().equals(other.stringRep());
		}
		else {
			return false;
		}
	}
}
