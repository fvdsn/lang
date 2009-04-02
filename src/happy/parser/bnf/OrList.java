package happy.parser.bnf;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provide a wrapper for the entire right part of a rule, 
 * the orlist is a list of cat list. 
 *
 */
public class OrList {
	List<CatList> list;
	
	public OrList() {
		list = new ArrayList<CatList>();
	}
	public void add(CatList t) {
		list.add(t);
	}
	
	
	public List<CatList> getTerm() {
		
		return list;
	}
	
	@Override
	public String toString() {
		
		
		
		boolean first = true;
		StringBuilder res = new StringBuilder("");
		for(Term t : list) {
			if(first) {
				res.append(t);
				first = false;
			} else
				res.append(" | " + t);
		}
		return res.toString();
	}
	
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

}
