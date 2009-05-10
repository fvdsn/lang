package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;

import java.util.ArrayList;
import java.util.List;

public class CheckSuffix {
	/**
	 * 
	 * @param rules The rules list
	 * @param table The precedence table
	 * @return a list of tuple (triple) that contains the two conflicting rules and the suffix the cause the conflict
	 */
	public static boolean check(List<Rule> rules) {
		
		List<RulesTuple> problem = new ArrayList<RulesTuple>(); 
		List<Pair> AllCat = new ArrayList<Pair>();
		
		/**
		 * Met tout les partie de droite des règles dans une liste avec la règle associée
		 */
		for(Rule r : rules) {
			for(CatList c : r.getOrList()) {
				AllCat.add(new Pair(c, r));
			}
		}
		
		/**
		 * on parcourt toutes les règles et on regarde si l'une est suffix de l'autre
		 */
		for(Rule r : rules) {
			for(CatList c : r.getOrList()) {
				for(Pair p : AllCat) {
					if(isSuffix(c, p.cat)) {
						
						//si une règle est suffix d'une autre, que ce n'est pas la même règle
						if(!(
								(p.cat.stringRep().equals(c.stringRep())) 
								&& 
								(r.getLeftSide().equals(p.r1.getLeftSide()))
							)) {
							
							problem.add(new RulesTuple(r, p.r1, c, p.cat));
													
						}
					}
				}
			}
		}
		
		for(RulesTuple r : problem) {
			System.out.println("conflit avec \n " + r.prob + "  <<>>  " + r.prob2 + "\n" + r.r1.getLeftSide() + "\n" + r.r2.getLeftSide() + " \n");
			
		}
		
		return problem.size() == 0;
	}
	
	/**
	 * Renvoie true si c2 est suffix c1
	 * @param c1
	 * @param c2
	 * @return
	 */
	private static boolean isSuffix(CatList c1, CatList c2) {
		int size = c2.getTermList().size();
		//System.out.println(c1);
		//System.out.println(c2);
		//ne peut être suffix car plus grand que l'autre
		if(size > c1.getTermList().size()) {
			return false;
		}
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
