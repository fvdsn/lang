package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;

import java.util.List;

public class CheckRevertible {
	public static boolean check(List<Rule> grammar){
		for(Rule r:grammar){
			for(CatList c: r.getOrList()){
				for(Rule r2:grammar){
					for(CatList c2: r2.getOrList()){
						if(c.match(c2)){
							if(!r.equals(r2)){
								System.out.println("Reversion problem :");
								System.out.println(r.getName() +  " ::= " + c.stringRep());
								System.out.println(r2.getName() + " ::= " + c2.stringRep());
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
}
