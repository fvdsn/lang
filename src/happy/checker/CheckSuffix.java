package happy.checker;

import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;



import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CheckSuffix {
	/**
	 * 
	 * @param rules The rules list
	 * @param table The precedence table
	 * @return a list of tuple (triple) that contains the two conflicting rules and the suffix the cause the conflict
	 */
	public static List<RulesTuple> checkSuffix(List<Rule> rules, Hashtable<Term,Hashtable<Term,String>> table) {
		
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
						if(!p.cat.stringRep().equals(c.stringRep())) {
							//on vérifie les critères de la table précédence. 
							
							CatList before = getTermBeforeSuffix(p.cat, c);
							CatList little = null;
							Term rule = null;
							if(before == p.cat) {
								rule = r.getLeftSide();
								
								little = c;
							}
							else if(before == c) {
								rule =  p.r1.getLeftSide();
								little = p.cat;
							}
								
							
							if(rule != null) {
								Term precedent = before.getTermList().get(before.getTermList().size() - 2);
								
								//conflit si suffix et que precedent < rule ou = , Leq étant les deux. 
								if(table.get(precedent).get(rule).equals(CheckPrecedence.EQ) 
										|| table.get(precedent).get(rule).equals(CheckPrecedence.LE)
										|| table.get(precedent).get(rule).equals(CheckPrecedence.LEQ)) {
									System.out.println("----------------------------");
									System.out.println(rule + "   " + precedent);
									System.out.println(table.get(precedent).get(rule));
									System.out.println("----------------------------");
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
	
	/**
	 * Renvoie true si c2 est suffix c1
	 * @param c1
	 * @param c2
	 * @return
	 */
	private static boolean isSuffix(CatList c1, CatList c2) {
		int size = c2.getTermList().size();
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
	
	/**
	 * 
	 * @param c1 catlist
	 * @param c2 catlist
	 * @return the catlist with the biggest number of Term, that one where we should take 
	 * the element before the suffix
	 */
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
