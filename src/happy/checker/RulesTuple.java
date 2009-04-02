package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;

public class RulesTuple {
	public Rule r1;
	public Rule r2;
	
	public CatList prob;
	
	public RulesTuple(Rule r1, Rule r2, CatList prob) {
		this.r1 = r1;
		this.r2 = r2;
		this.prob = prob;
	}
}
