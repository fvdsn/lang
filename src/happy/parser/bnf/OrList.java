package happy.parser.bnf;

import java.util.ArrayList;
import java.util.List;

public class OrList {
	List<Term> list;
	CatList catlist;
	
	public OrList() {
		list = new ArrayList<Term>();
	}
	public void add(Term t) {
		list.add(t);
	}
	
	public CatList getCatList() {
		/* TODO implement */
		return catlist;
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

}
