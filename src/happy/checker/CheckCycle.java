package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CheckCycle {
	public static List<RulesTuple> checkSuffix(List<Rule> rules, Hashtable<Term,Hashtable<Term,String>> table) {
		List<RulesTuple> problem = new ArrayList<RulesTuple>();
		List<Pair> AllCat = new ArrayList<Pair>();
		
		for(Rule r : rules) {
			for(CatList c : r.getOrList()) {
				AllCat.add(new Pair(c, r));
			}
		}
		
		for(Rule r : rules) {
			for(CatList c : r.getOrList()) {
				for(Pair p : AllCat) {
					if(isSuffix(c, p.cat)) {
						if(!p.cat.stringRep().equals(c.stringRep())  && !r.getLeftSide().equals(p.r1.getLeftSide())) {
							CatList before = getTermBeforeSuffix(p.cat, c);
							CatList little = null;
							Term rule = null;
							if(before == p.cat) {
								rule =  p.r1.getLeftSide();
								little = c;
							}
							else if(before == c) {
								rule = r.getLeftSide();
								little = p.cat;
							}
								
							if(rule != null) {
								Term precedent = before.getTermList().get(before.getTermList().size() - 2);
								if(table.get(rule).get(precedent).equals(CheckPrecedence.EQ) || table.get(rule).get(precedent).equals(CheckPrecedence.LE)) {
									problem.add(new RulesTuple(r, p.r1, little));
								}
							}
							
							
							
						}
					}
				}
			}
		}
		
		
		return problem;
	}
	
	
	private static boolean isSuffix(CatList c1, CatList c2) {
		int size = c2.getTermList().size();
		//ne peut être suffix car plus grand que l'autre
		if(size > c1.getTermList().size()) {
			
			return false;
		}
		//System.out.println(c1.getTermList().size() + "  =>> "  + size );
		//System.out.println(c1.toString() + "=>>>> " + c2.toString());
		int compteur = size - 1;
		int cpt1 = c1.getTermList().size() - 1;
		boolean suffix = true;
		while(compteur >= 0 && suffix) {
			suffix = c1.getTermList().get(cpt1).toString().equals(c2.getTermList().get(compteur).toString());
			compteur--;
			cpt1--;
		}
		return suffix;
	}
	
	
	private static CatList getTermBeforeSuffix(CatList c1, CatList c2) {
		int size1 = c1.getTermList().size();
		int size2 = c2.getTermList().size();
		if(size1 > size2) {
			return c1;
		}
		else if(size1 < size2) {
			return c2;
		}
		else 
			return null;
		
	}
	
	
}
