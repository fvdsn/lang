package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class CheckAccess {
	public static boolean check(List<Rule> grammar) {
		List<Rule> explored = new ArrayList<Rule>();
		List<Term> allTerm = CheckPrecedence.getAllTerm(grammar);
		Set<Term> find = new HashSet<Term>();
		
		find.add(grammar.get(0).getLeftSide());
		explored.add(grammar.get(0));
		explore(find, grammar.get(0), grammar, explored);
		for(Term t : find) {
			allTerm.remove(t);
		}
		for(Term t : allTerm) {
			System.out.println(t + " est inaccessible");
		}
		return allTerm.size() == 0;
		
	}
	
	private static void explore(Set<Term> find, Rule r, List<Rule> grammar, List<Rule> exp) {
		
		for(CatList c : r.getOrList()) {
			for(Term t : c.getTermList()) {
				find.add(t);
				if(!t.isTerminal()) {
					for(Rule r1 : grammar) {
						if(r1.getLeftSide().equals(t)) {
							if(!exp.contains(r1)) { 
								exp.add(r1);
								explore(find, r1, grammar, exp);
							}
						}
					}
				}
				
			}
		}
		
	}
}
