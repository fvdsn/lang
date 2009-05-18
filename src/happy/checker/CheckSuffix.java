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
	public static boolean check(List<Rule> grammar){
		Hashtable<Term,Hashtable<Term,String>> prectable = CheckPrecedence.createTable(grammar);
		CheckPrecedence.precTable(grammar, prectable);
		for(Rule r1:grammar){
			for(CatList c1: r1.getOrList()){
				for(Rule r2:grammar){
					for(CatList c2:r2.getOrList()){
						if(r1 == r2 && c1.match(c2)){
							continue;
						}
						if(r1 != r2 && c1.match(c2)){
							System.out.println("Syntax not reversible");
							return false;
						}
						if(isSuffix(c2,c1)){	//c1 is suffix of c2
							/*
							 * A -> 'c1'
							 * C -> 'x D c1' == 'c2'
							 * 
							 */
							Term A = r1.getLeftSide();
							Term D = c2.getTermList().get(c2.getTermList().size() - c1.getTermList().size());
							if(   prectable.get(D).get(A).equals(CheckPrecedence.EQ)
								||prectable.get(D).get(A).equals(CheckPrecedence.LE)
								||prectable.get(D).get(A).equals(CheckPrecedence.LEQ) ){
								System.out.println("Suffix error :");
								System.out.println(r1.getLeftSide() + "::=" + c1.toString());
								System.out.println(r2.getLeftSide() + "::=" + c2.toString());
							}
						}
					}
				}
			}
		}
		return true;
	}
	/*public static boolean check(List<Rule> rules) {
		
		List<RulesTuple> problem = new ArrayList<RulesTuple>(); 
		List<Pair> AllCat = new ArrayList<Pair>();
		
		/**
		 * Met tout les partie de droite des règles dans une liste avec la règle associée
		 /
		for(Rule r : rules) {
			for(CatList c : r.getOrList()) {
				AllCat.add(new Pair(c, r));
			}
		}
		
		/**
		 * on parcourt toutes les règles et on regarde si l'une est suffix de l'autre
		 /
		for(Rule r : rules) {
			for(CatList c : r.getOrList()) {
				for(Pair p : AllCat) {
					if(isSuffix(c, p.cat)) {
						
						//si une règle est suffix d'une autre, que ce n'est pas la même règle
						if(	   !(  (p.cat.stringRep().equals(c.stringRep())) 
								&& 
								  (r.getLeftSide().equals(p.r1.getLeftSide())))
							 || ()
						)
						{
							
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
	}*/
	
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
