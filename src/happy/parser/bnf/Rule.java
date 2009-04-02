package happy.parser.bnf;

import java.util.List;

import happy.parser.util.CharIdentifier;

/**
 * This class provide a tool to parse a single line of a bnf file and transform it into a rule
 * @author mythrys
 *
 */
public class Rule {
	String name;
	OrList list;
	
	
	public Rule(String r) throws NotRuleException {
		//if the line is empty this is not a rule
		if(r == null || r.equals(""))
			throw new NotRuleException();
		
		list = new OrList();
		
		
		
		parse(r.toCharArray());
		
		

	}
	
	/**
	 * 
	 * @param r un tableau de caractère, qui doit être conforme à la notation bnf suivante
	 * <nonTerminal> ::= <nonTerminal> 'terminal' | '\'' '\\' | <nonTerminal>
	 */
	private void parse(char[] r) {
		/*
		 * 0) trouver la partie gauche de la règle qu'on appel le nom.
		 * 1) parcourt tout les caractère quand on tombe sur un < on débute la découverte d'un symbole non terminal 
		 * 	et > on a trouver le symbole nonterminal qu'on ajoute dans la catliste courant
		 */
		boolean nonTerminal = false;
		boolean name = true;
		boolean terminal = false;
		boolean chara = false;
		String token = "";
		CatList cat = new CatList();
		for(char c : r) {
			//tout les espaces qui ne se trouvent pas dans un symbole non terminal sont ignorés
			if(!CharIdentifier.isSpace(c)) {
				//début d'un symbole non terminal
				if(c == '<') {
					nonTerminal = true;
					token = "";
				}
				//fin d'un symbole terminal
				else if(c == '>' && nonTerminal && !terminal) {
					nonTerminal = false;
					if(name) {
						name = false;
						this.name = token;
					}
					else {
						//ajout du symbole dans la cat liste
						cat.add(new TermImpl(token, false));
						token = "";
					}
					
				}
				
				//debut d'un symbole terminal (' pas précédé par \)
				else if(c == '\'' && !terminal && !chara) {
					terminal = true;
					token = "";
				}
				//fin d'un symbole terminal 
				else if(c == '\'' && terminal && !chara) {
					terminal = false;
					//cas particulier, a..z, A..Z, 0..9 sont écris en intension, mise en expension
					if(token.equals("a..z")) {
						addLetter(list);
					}
					else if(token.equals("A..Z")) {
						addCapitalLetter(list);
					}
					else if(token.equals("0..9")) {
						addDigit(list);
					}
					else {
						//ajout du symbole dans la cat liste
						cat.add(new TermImpl(token, true));
					}
					token = "";
				}
				
				//gestion des caractère spéciaux ' et \
				else if(c == '\'' && terminal && chara) {
					token = token + c;
					chara = false;
				}				
				else if(c == '\\' && !chara) {
					chara = true;
				}
				else if(c == '\\' && terminal && chara) {
					token = token + c;
					chara = false;
				}
				
				//lorsqu'on trouve un | entre ''
				else if(c == '|' && terminal) {
					token = token + c;
				}
				//losrqu'on arrive à la fin d'une règle de réécriture
				else if(c == '|' && !terminal) {
					list.add(cat);
					cat = new CatList();
				}
				//tout les autres cas
				else {
					if(nonTerminal || terminal)
						token = token + c;
				}
			}
			//espace dans un symbole terminal
			else if(CharIdentifier.isSpace(c) && terminal) {
				token = token + c;
			}
		}
		//ajout de la dernière catlist car pas de | pour marquer la fin de la dernière
		this.list.add(cat);
	
	}
	

	private void addLetter(OrList l) {
		String tab[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		for(String t : tab) {
			CatList c = new CatList();
			c.add(new TermImpl(t, true));
			l.add(c);
		}
		
	}
	
	private void addCapitalLetter(OrList l) {
		String tab[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for(String t : tab) {
			CatList c = new CatList();
			c.add(new TermImpl(t, true));
			l.add(c);
		}
	}
	
	private void addDigit(OrList l) {
		String tab[] = {"0","1","2","3","4","5","6","7","8","9"};
		for(String t : tab) {
			CatList c = new CatList();
			c.add(new TermImpl(t, true));
			l.add(c);
		}
	}
	public String toString() {
		
		return name + " ::= " + list.toString();
	}
	
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	public List<CatList> getOrList(){
		
		return list.getTerm();
	}
	public Term getLeftSide(){
		return new TermImpl(name, false);
	}
}
