package happy.checker;

import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;

import java.util.ArrayList;
import java.util.List;

public class CheckUtility {
	public static boolean check(List<Rule> grammar) {
		List<Term> term = CheckPrecedence.getAllTerm(grammar);
		List<Term> nT = new ArrayList<Term>();
		for(Term t:term){
			if(!t.isTerminal()) {
				nT.add(t);

			}
		}
		
		for(Rule r : grammar) {
			Term  temp = null;
			for(Term t : nT) {
				if(t.equals(r.getLeftSide())) {
					temp = t;
				}
				
			}
			if(temp != null) {
				nT.remove(temp);
			}
		}
		if(!nT.isEmpty()){
			System.out.println("Symboles inutile :");
			for(Term t : nT) {
				System.out.println(t);
			}
		}
		return nT.size() == 0;
		
	}
}
