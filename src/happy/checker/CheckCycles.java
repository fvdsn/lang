package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class CheckCycles {
	public static boolean check(List<Rule> grammar){
		HashSet<Term> Nodes = new HashSet<Term>();
		Hashtable<Term,HashSet<Term>> Edges = new Hashtable<Term,HashSet<Term>>();
		/*constructing graph */
		for(Rule r:grammar){
			Term tr = r.getLeftSide();
			Nodes.add(tr);
			Edges.put(tr,new HashSet<Term>());
			for(CatList c:r.getOrList()){
				for(Term t:c.getTermList()){
					if (!t.equals(tr)){
						if(!t.isTerminal()){
							Edges.get(tr).add(t);
						}
					}
				}
			}
		}
		/* checking for cycles */
		List<Term>Ends = new LinkedList<Term>();
		Hashtable<Term,Integer> EdgeCount = new Hashtable<Term,Integer>();
		for(Term n:Nodes){
			if (Edges.get(n).isEmpty()){
				Ends.add(n);
			}
			EdgeCount.put(n,Edges.get(n).size());
		}
		while(!Ends.isEmpty()){
			Term end = Ends.remove(0);
			for(Term start:Nodes){
				if(EdgeCount.get(start) > 0 && Edges.get(start).contains(end)){
					EdgeCount.put(start,EdgeCount.get(start)-1);
					if(EdgeCount.get(start) == 0){
						Ends.add(start);
					}
				}
			}
		}
		for(Term t:Nodes){
			if(EdgeCount.get(t) > 0){
				System.out.println("Found cycle involving "+t);
				return false;
			}
		}
		return true;
	}
}
