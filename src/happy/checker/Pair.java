package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;

public class Pair {
	public Rule r1;
	public CatList cat;
	
	public Pair(CatList c, Rule r1) {
		this.r1 = r1;
		this.cat = c;
	}
}
