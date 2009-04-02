package happy.checker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import happy.parser.bnf.*;


public class CheckPrecedence {
	public static String NOTHING = " ";
	public static String LE 	= "<.";
	public static String LEQ 	= "<="; 
	public static String GE 	= ".>";
	public static String EQ 	= ".=";
	public static String ERROR  = "X";
	public static void fprintn(String s,int n){
		char[] cs = s.toCharArray();
		int i = 0;
		while(i < n){
			if(i < cs.length){
				System.out.print(cs[i]);
			}else{
				System.out.print(" ");
			}
			i++;
		}
	}
	public static List<Term> getAllTerm(List<Rule> grammar){
		Set<Term> set = new HashSet<Term>();
		List<Term> list = new ArrayList<Term>();
		for(Rule r:grammar){
			set.add(r.getLeftSide());
			for(CatList cl: r.getOrList()){
				for(Term t:cl.getTermList()){
					set.add(t);
				}
			}
		}
		for(Term t:set){
			list.add(t);
		}
		return list;
	}
	public static void printSet(Hashtable<Term,Set<Term>> set){
		for( Term key:set.keySet()){
			System.out.print(key.toString());
			System.out.print("-> ");
			for(Term el:set.get(key)){
				fprintn(el.toString(),4);
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
	public static boolean tableSet(Hashtable<Term,Hashtable<Term,String>> table, Term X, Term Y, String rel, Rule rule){
		String old_rel = table.get(X).get(Y);
		if(old_rel.equals(NOTHING) || old_rel.equals(rel)){
			table.get(X).put(Y, rel);
		}else if(old_rel.equals(LEQ)){
			if(rel.equals(LE) || rel.equals(EQ)){
				return true;
			}
		}else if(old_rel.equals(LE)){
			if(rel.equals(EQ)){
				table.get(X).put(Y, LEQ);
			}
		}else if(old_rel.equals(EQ)){
			if(rel.equals(LE)){
				table.get(X).put(Y,LEQ);
			}
		}else if(old_rel.equals(ERROR)){
			return false;
		}else{
			System.out.println("Conflict from Rule : "+rule.toString());
			System.out.println("Was :"+X.toString()+old_rel+Y.toString());
			System.out.println("Is  :"+X.toString()+rel+Y.toString());
			table.get(X).put(Y, ERROR);
			return false;
		}
		return true;
	}
	public static void printTable(Hashtable<Term,Hashtable<Term,String>> table, List<Term> allTerm){
		fprintn("    |",5);
		for(Term t:allTerm){
			fprintn(t.toString(),4);
			fprintn("|",1);
		}
		System.out.println("");
		for(Term c:allTerm){
			fprintn("_____",5);
		}
		System.out.println("");
		for(Term line:allTerm){
			fprintn(line.toString(),4);
			fprintn("|",1);
			for(Term col:allTerm){
				fprintn(" "+table.get(line).get(col),4);
				fprintn(":",1);
			}
			System.out.println("");
			for(Term col:allTerm){
				fprintn("-----",5);
			}
			System.out.println("");
		}
		
	}
	public static Hashtable<Term,Set<Term>> FirstSet(List<Rule> grammar){
		Hashtable<Term,Set<Term>> First = new Hashtable<Term, Set<Term>>();
		HashSet<Term> AllTerm = new HashSet<Term>();
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
	public static Hashtable<Term,Hashtable<Term,String>> precTable(List<Rule> grammar){
		Hashtable<Term,Set<Term>> First = FirstSet(grammar);
		Hashtable<Term,Set<Term>> Last = LastSet(grammar);
		Hashtable<Term,Hashtable<Term,String>> table = new Hashtable();
		List<Term> allTerm = getAllTerm(grammar);
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
		/*0*/
		for(Term l: allTerm){
			Hashtable<Term,String> line = new Hashtable<Term,String>();
			table.put(l,line);
			for(Term col:allTerm){
				line.put(col, NOTHING);
			}
		}
		/*1*/
		for(Rule r:grammar){
			for(CatList cl:r.getOrList()){
				List<Term> tl = cl.getTermList();
				Term X,Y;
				int i = 0;
				int n = tl.size();
				while(i < n-1){
					X = tl.get(i);
					Y = tl.get(i+1);
					if(X.isTerminal() && Y.isTerminal()){
						/*2*/
						tableSet(table,X,Y,EQ,r);
					}
					if(!X.isTerminal()){
						/*3*/
						for(Term s:Last.get(X)){
							if(!Y.isTerminal()){
								for(Term t:First.get(Y)){
									if(t.isTerminal()){
										tableSet(table,s,t,GE,r);
									}
								}
							}else{
								tableSet(table,s,Y,GE,r);
							}
						}
					}
					if(!Y.isTerminal()){
						for(Term s:First.get(Y)){
							tableSet(table, X, s, LE, r);
						}
					}
					
					i++;
				}
			}
		}
		return null;
	}
}
