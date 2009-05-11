package happy.parser.newlexical;

import happy.parser.bnf.Term;
import happy.parser.util.CharIdentifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LexicalParser implements Iterable<Term>, Iterator<Term> {

	int level = 0;
	BufferedReader reader;
	boolean hasNext = true;
	LexicalTerm next = null;
	
	public LexicalParser(String file) throws FileNotFoundException {
		File f = new File(file);
		this.reader = new BufferedReader(new FileReader(f));
	}
	
	

	@Override
	public Iterator<Term> iterator() {
		
		return this;
	}
	
	@Override
	public boolean hasNext() {
		
		return hasNext;
	}
	
	@Override
	public Term next()  {
		if(next != null) {
			LexicalTerm temp = next;
			next = null;
			return temp;
		}
		boolean start = true;
		StringBuilder str = new StringBuilder();
		String term = "";
		String lexicalTerm = "";
		while(true) {
			try {
				int c = this.reader.read();
			
			
				if(c == -1) {
					
					hasNext = false;
					if(str.length() == 0) {
						return null;
					}
					return new LexicalTerm(findTerm(str.toString()), true, findLexicalTerm(str.toString()), str.toString());
				}
				char ca = (char) c;
				//System.out.println(ca);
				if(CharIdentifier.isSpace(ca)) {
					if(!start) {
					
						return new LexicalTerm(findTerm(str.toString()), true, findLexicalTerm(str.toString()), str.toString());
					}					
				}
				else {
					int cha = CharIdentifier.isReserved(ca);
					if(cha == -1) {
						start = false;
						str.append(ca);
					}
					else {
						
						char car = (char) cha;
						
						if(str.length() == 0) {
							if(car == '(') {
								
								level++;
							}
							if(car == ')') {
								level--;
							}
							return new LexicalTerm(new Character(car).toString(), true, new Character(car).toString(), new Character(car).toString());
						}
						next = new LexicalTerm(new Character(car).toString(), true, new Character(car).toString(), new Character(car).toString());
						LexicalTerm t = new LexicalTerm(findTerm(str.toString()), true, findLexicalTerm(str.toString()), str.toString());
						if(car == '(') {							
							level++;
						}
						if(car == ')') {
							level--;
						}
						return t;
					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
				hasNext = false;
				return null;
			}
		}
	}



	@Override
	public void remove() throws NotImplementedException {
		throw new NotImplementedException();		
	}
	
	/**
	 * On sait qu'on a pas affaire au paranthèse donc, c'est forcément un id
	 * reste à connaitre le niveau
	 * @param value
	 * @return
	 */
	private String findTerm(String value) {
		return "l" + level + "_id";
	}
	
	private String findLexicalTerm(String value) {
		return "";
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		LexicalParser p = new LexicalParser("test1");
		
		for(Term t : p) {
			if(t != null) {
				System.out.println(t.getValue() + "  " + t.getType());
				
			}
		}
	}
}
