package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.util.CharIdentifier;

import java.util.ArrayList;
import java.util.List;

public class CheckCycle {
	public static List<RulesTuple> checkSuffix(List<Rule> rules) {
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
							problem.add(new RulesTuple(r, p.r1, c));
						}
					}
				}
			}
		}
		
		
		return problem;
	}
	
	
	public static boolean isSuffix(CatList c1, CatList c2) {
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
}
