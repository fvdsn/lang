package happy.checker;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import happy.parser.bnf.*;

public class CheckPrecedence {
	public static char NOTHING = 0;
	public static char LE = '<';
	public static char LEQ = '(';
	public static char GE = '>';
	public static char EQ = '=';
	public static Hashtable<Term,Set<Term>> FirstSet(List<Rule> grammar){
		Hashtable<Term,Set<Term>> First = new Hashtable<Term, Set<Term>>();
		HashSet<Term> AllTerm = new HashSet();
		boolean done = false;
		/*
		 * 1) Pour tout Non terminal A de la grammaire -> 
		 * 	  On identifie tout les cotés droit.
		 * 	  - pour tous ces cotés droits on identifie
		 * 		ceux qui terminent par un terminal. 
		 * 	    - On ajoute tous ces terminaux à Last(A)
		 * 2) Pour tout non terminal A de la grammaire ->
		 * 	  On identifie tous les cotés droit.
		 *    - pour tous ces cotés droits on identifie 
		 *      ceux qui terminent par un non terminal B.
		 *      - On ajoute Last(B) à Last(A) 
		 * 3) répéter 2) tant que les Last changent.  
		 */
		for(Rule r:grammar){
			AllTerm.add(r.getLeftSide());
			First.put(r.getLeftSide(), new HashSet<Term>());
		}
		/*1)*/
		for(Term t: AllTerm){
			for(Rule r:grammar){
				if(t == r.getLeftSide()){
					for(CatList cl: r.getOrList()){
						if(cl.getFirst().isTerminal()){
							First.get(t).add(cl.getFirst());
						}
					}
				}
			}
		}
		/*3)*/
		while(!done){
			done = true;
			/*2)*/
			for(Term t: AllTerm){
				for(Rule r:grammar){
					if(t == r.getLeftSide()){
						for(CatList cl: r.getOrList()){
							Term fst = cl.getFirst();
							if(!fst.isTerminal()){
								if(First.get(t).addAll(First.get(fst))){
									done = false;
								}
							}
						}
					}
				}
			}
		}
		return First;		
	}
	public static Hashtable<Term,Set<Term>> LastSet(List<Rule> grammar){
		Hashtable<Term,Set<Term>> Last = new Hashtable<Term, Set<Term>>();
		HashSet<Term> AllTerm = new HashSet();
		boolean done = false;
		/*
		 * 1) Pour tout Non terminal A de la grammaire -> 
		 * 	  On identifie tout les cotés droit.
		 * 	  - pour tous ces cotés droits on identifie
		 * 		ceux qui commencent par un terminal. 
		 * 	    - On ajoute tous ces terminaux à First(A)
		 * 2) Pour tout non terminal A de la grammaire ->
		 * 	  On identifie tous les cotés droit.
		 *    - pour tous ces cotés droits on identifie 
		 *      ceux qui commencent par un non terminal B.
		 *      - On ajoute First(B) à First(A) 
		 * 3) répéter 2) tant que les First changent.  
		 */
		for(Rule r:grammar){
			AllTerm.add(r.getLeftSide());
			Last.put(r.getLeftSide(), new HashSet<Term>());
		}
		/*1)*/
		for(Term t: AllTerm){
			for(Rule r:grammar){
				if(t == r.getLeftSide()){
					for(CatList cl: r.getOrList()){
						if(cl.getLast().isTerminal()){
							Last.get(t).add(cl.getLast());
						}
					}
				}
			}
		}
		/*3)*/
		while(!done){
			done = true;
			/*2)*/
			for(Term t: AllTerm){
				for(Rule r:grammar){
					if(t == r.getLeftSide()){
						for(CatList cl: r.getOrList()){
							Term lst = cl.getLast();
							if(!lst.isTerminal()){
								if(Last.get(t).addAll(Last.get(lst))){
									done = false;
								}
							}
						}
					}
				}
			}
		}
		return Last;		
	}
	public static char[][] precTable(List<Rule> grammar){
		Hashtable<Term,Set<Term>> First = FirstSet(grammar);
		Hashtable<Term,Set<Term>> Last = LastSet(grammar);
		/*
		 * 0) On initialise la table à NOTHING
		 * 1) On identifie toutes les parties droites.
		 * 	  - Pour chacune d'entre elles, on identifie toutes
		 *      les paires de terminaux cote à cote XY.
		 * 2) On met X =. Y.
		 * 3) Si X non terminal 
		 *      -> pour tout symbole s dans Last(X),
		 *         -> pour tout terminal t dans First (Y) 
		 *         -> pour Y si Y terminal
		 *         	  s .> t
		 * 4) Si Y non terminal
		 * 		-> pour tout symbole s dans First(Y) 
		 * 			  X <. S
		 * 5) Si jamais la mise à jour d'un opérateur présente
		 *    un conflit -> on imprime le conflit. Si le conflit
		 *    est entre <. et =. On met un <=. 
		 *    Si pas de conflits -> OK ! 
		 */
		return null;
	}
}
