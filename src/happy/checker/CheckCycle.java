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
					if(CharIdentifier.isSuffix(p.cat.stringRep(), c.stringRep())) {
						problem.add(new RulesTuple(r, p.r1));
					}
				}
			}
		}
		
		
		return problem;
	}
}
