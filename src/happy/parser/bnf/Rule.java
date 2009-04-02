package happy.parser.bnf;

import java.util.List;

import happy.parser.util.CharIdentifier;

public class Rule {
	String name;
	OrList list;
	
	
	public Rule(String r) throws NotRuleException {
		if(r == null || r.equals(""))
			throw new NotRuleException();
		
		list = new OrList();
		
		
		
		parse(r.toCharArray());
		
		

	}
	
	private void parse(char[] r) {
		//1 find name
		boolean id = false;
		boolean name = true;
		boolean terminal = false;
		boolean chara = false;
		String token = "";
		CatList cat = new CatList();
		for(char c : r) {
			
			if(!CharIdentifier.isSpace(c)) {
				if(c == '<') {
					id = true;
					token = "";
				}
				else if(c == '>' && id && !terminal) {
					id = false;
					if(name) {
						name = false;
						this.name = token;
					}
					else {
						cat.add(new TermImpl(token, false));
						token = "";
					}
					
				}
				
				
				else if(c == '\'' && !terminal && !chara) {
					terminal = true;
					token = "";
				}
				else if(c == '\'' && terminal && !chara) {
					terminal = false;
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
						cat.add(new TermImpl(token, true));
					}
					token = "";
				}
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
				
				
				else if(c == '|' && terminal) {
					token = token + c;
				}
				else if(c == '|' && !terminal) {
					list.add(cat);
					cat = new CatList();
				}
				else {
					if(id || terminal)
						token = token + c;
				}
			}
			else if(CharIdentifier.isSpace(c) && terminal) {
				token = token + c;
			}
		}
		this.list.add(cat);
	
	}
	

	private void addLetter(OrList l) {
		String tab[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","k","r","s","t","u","v","w","x","y","z"};
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

	public List<CatList> getOrList(){
		/* TODO implement*/
		return null;
	}
	public Term getLeftSide(){
		/* TODO implement */
		return null;
	}
}
