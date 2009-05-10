package happy.checker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import happy.parser.bnf.*;


public class CheckPrecedence {
	public static String NOTHING = "_";		/* aucune relation */
	public static String LE 	= "<.";		
	public static String LEQ 	= "<="; 
	public static String GE 	= ".>";
	public static String EQ 	= ".=";
	public static String ERROR  = "X" ;		/* conflit */
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
	/**
	 * Regarde si la grammaire n'a pas de conflits de précédence.
	 * Affiche la table des précédence à la sortie standard.
	 * @param grammar Une grammaire cohérente : tous les non terminaux ont au moins une règle. 
	 * @return true si la grammaire est valide, false sinon.
	 */
	public static boolean checkPrecedence(List<Rule> grammar){
		boolean validity = false;
		Hashtable<Term,Hashtable<Term,String>> table = null;
		List<Term> allTerm = getAllTerm(grammar);
		
		
		
		/* Print all terms */
		System.out.println("\nList of all Terms");
		for(Term t:allTerm){
			System.out.print(t.toString()+" ");
		}
		System.out.println("\nList of all Terminals");
		for(Term t:allTerm){
			if(t.isTerminal()){
				System.out.print(t.toString()+" ");
			}
		}
		System.out.println("\nList of all Non-Terminals");
		for(Term t:allTerm){
			if(!t.isTerminal()){
				System.out.print(t.toString()+" ");
			}
		}
		System.out.println("");
		System.out.println("\nPrecedence Table");
		/*Compute precedence table*/
		table = new Hashtable<Term,Hashtable<Term,String>>();
		for(Term l: allTerm){
			Hashtable<Term,String> line = new Hashtable<Term,String>();
			table.put(l,line);
			for(Term col:allTerm){
				line.put(col, NOTHING);
			}
		}
		validity = precTable(grammar,table);
		printTable(table,allTerm);
		return validity;
	}
	/**
	 * Renvoie la liste de tous les Termes d'une grammaire.
	 * @param grammar Une grammaire . 
	 * @return la liste de tous les Termes, avec chaque Terme ne s'y trouvant pas
	 * plus d'une fois.
	 */
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
	/**
	 * Imprime à la sortie standard l'ensemble First ou Last.
	 * @param set un Ensemble First ou Last.
	 */
	public static void printSet(Hashtable<Term,Set<Term>> set){
		for( Term key:set.keySet()){
			System.out.print(key.toString());
			System.out.print("-> ");
			for(Term el:set.get(key)){
				System.out.print(el.toString() + " ");
			}
			System.out.print("\n");
		}
	}
	/**
	 * Met une relation de précédence dans la table de précédence et
	 * résout les conflits. Affiche les détails de l'erreur à la sortie
	 * standard en cas de conflits. Si le conflit est entre <. ou <.= ou =. 
	 * celui ci est résolu en insérant un <.= 
	 * 
	 * @param table la table de précédence
	 * @param X	le premier Terme
	 * @param Y le second Terme
	 * @param rel la relation de précédence ( EQ,LE,GE,LEQ,ERROR,NOTHING)
	 * @param rule la règle d'ou provient X et Y.
	 * @return true si il n'y a pas de conflits, false sinon. 
	 */
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
			System.out.println("Conflict in Rule:" + rule.getLeftSide());
			System.out.println("   Was :"+X.toString()+old_rel+Y.toString());
			System.out.println("   Is  :"+X.toString()+rel+Y.toString());
			table.get(X).put(Y, ERROR);
			return false;
		}
		return true;
	}
	/**
	 * Imprime une table de précédence à la sortie standard.
	 * @param table la table de précédence, non nulle.
	 * @param allTerm : tous les termes de la table. 
	 */
	public static void printTable(Hashtable<Term,Hashtable<Term,String>> table, List<Term> allTerm){
		fprintn("    |",5);
		for(Term t:allTerm){
			fprintn(t.toString(),4);
			fprintn("|",1);
		}
		System.out.println("");
		System.out.print("     ");
		for(Term c: allTerm){
			fprintn("=====",5);
		}
		System.out.println("");
		for(Term line:allTerm){
			fprintn(line.toString(),4);
			fprintn("|",1);
			for(Term col:allTerm){
				fprintn(" "+table.get(line).get(col),4);
				fprintn("|",1);
			}
			System.out.println("");
			fprintn("     ",5);
			for(Term col:allTerm){
				fprintn("-----",5);
			}
			System.out.println("");
		}
		
	}
	/**
	 * Calcule l'ensemble First pour chaque Terme correspondant aux Termes
	 * pouvant se trouver à l'extrème gauche de celui ci. 
	 * @param grammar la grammaire
	 * @return une Hashtable qui fait correspondre à chaque Terme son set First.
	 */
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
			//System.out.println("t");
			for(Rule r:grammar){
				//System.out.println("r");
				if(t.equals(r.getLeftSide())){
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
					if(t.equals(r.getLeftSide())){
						for(CatList cl: r.getOrList()){
							Term fst = cl.getFirst();
							if(!fst.isTerminal()){
								if(First.get(t).add(fst)){
									done = false;
								}
								if(First.get(fst) == null){
									System.out.println("Not in First:"+ fst.toString());
								}else if(First.get(t).addAll(First.get(fst))){
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
	/**
	 * Calcule l'ensemble Last pour chaque Terme correspondant aux Termes
	 * pouvant se trouver à l'extrème droite de celui ci. 
	 * @param grammar la grammaire
	 * @return une Hashtable qui fait correspondre à chaque Terme son set Last.
	 */
	public static Hashtable<Term,Set<Term>> LastSet(List<Rule> grammar){
		Hashtable<Term,Set<Term>> Last = new Hashtable<Term, Set<Term>>();
		HashSet<Term> AllTerm = new HashSet<Term>();
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
				if(t.equals(r.getLeftSide())){
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
					if(t.equals(r.getLeftSide())){
						for(CatList cl: r.getOrList()){
							Term lst = cl.getLast();
							if(!lst.isTerminal()){
								if(Last.get(t).add(lst)){
									done = false;
								}
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
	/**
	 * Calcule la table de précédence WP et si celle ci contient des conflits.
	 * En cas de conflits, ceux ci sont affichés à la sortie standard.
	 * @param grammar la grammaire
	 * @param table une Hashtable à deux dimensions dont les clefs sont tous les couples
	 * 			de termes existant dans grammar. Tous les éléments de table sont initialisés
	 * 			à NOTHING.
	 * @return true si il n'y a pas de conflits dans la table, false sinon. 
	 */
	public static  void printError(Rule r ,List<Term> termlist, int pos, Term X, Term Y){
		System.out.println("Error in rule:"+ r.getLeftSide());
		System.out.println("   Pos:"+pos);
		System.out.println("   XY:"+X+" "+Y);
	}
	public static Hashtable<Term,Hashtable<Term,String>> createTable(List<Rule> grammar){
		List<Term> allTerm = getAllTerm(grammar);
		Hashtable<Term,Hashtable<Term,String>> prectable = new Hashtable<Term,Hashtable<Term,String>>();
		for(Term l: allTerm){
			Hashtable<Term,String> line = new Hashtable<Term,String>();
			prectable.put(l,line);
			for(Term col:allTerm){
				line.put(col, CheckPrecedence.NOTHING);
			}
		}
		return prectable;
	}
	public static  boolean precTable(List<Rule> grammar, Hashtable<Term,Hashtable<Term,String>> table){
		Hashtable<Term,Set<Term>> First = FirstSet(grammar);
		Hashtable<Term,Set<Term>> Last = LastSet(grammar);
		//List<Term> allTerm = getAllTerm(grammar);
		boolean validity = true;
		/*
		 * 0) La table est initialisée à NOTHING.
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
					/*5*/
					if(!tableSet(table,X,Y,EQ,r)){  
						validity = false;
						printError(r,tl,i,X,Y);
					}
					/*if(X.isTerminal() && Y.isTerminal()){
						*2*
						
					}*/
					if(!X.isTerminal()){
						/*3*/
						if(Last.get(X) == null){
							System.out.println("Is not in Last :"+X.toString());
						}else{
						for(Term s:Last.get(X)){
							if(!Y.isTerminal()){
								for(Term t:First.get(Y)){
									if(t.isTerminal()){
										/*5*/
										if(!tableSet(table,s,t,GE,r)){validity = false;printError(r,tl,i,X,Y);}
									}
								}
							}else{
								/*5*/
								if(!tableSet(table,s,Y,GE,r)){validity = false;printError(r,tl,i,X,Y);}
							}
						}
						}
					}
					/*4*/
					if(!Y.isTerminal()){
						if(First.get(Y) == null){
							System.out.println("Is not in First:"+Y.toString());
						}else{
							for(Term s:First.get(Y)){
								/*5*/
								if(!tableSet(table, X, s, LE, r)){validity = false;printError(r,tl,i,X,Y);}
							}
						}
					}
					
					i++;
				}
			}
		}
		return validity;
	}
}
