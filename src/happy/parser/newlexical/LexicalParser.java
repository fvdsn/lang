package happy.parser.newlexical;

import happy.parser.bnf.Term;
import happy.parser.util.CharIdentifier;
import happy.parser.util.WordIdentifier;

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




	public Iterator<Term> iterator() {

		return this;
	}

	public boolean hasNext() {

		return hasNext;
	}


	public Term next()  {
		if(next != null) {
			LexicalTerm temp = next;
			next = null;
			return temp;
		}
		boolean start = true;
		boolean comment = false;
		StringBuilder str = new StringBuilder();

		while(true) {
			try {
				int c = this.reader.read();


				if(c == -1) {

					hasNext = false;
					if(level != 0) {
						System.out.println("Unexpected end");
						System.exit(0);
					}
					if(str.length() == 0) {
						return null;
					}

					return new LexicalTerm(findTerm(str.toString()), findLexicalTerm(str.toString()), str.toString());
				}
				char ca = (char) c;
				
				if(ca=='['){
					comment = true;
					
				}
				
				if(!comment){

					if(CharIdentifier.isSpace(ca)) {
						if(!start) {

							return new LexicalTerm(findTerm(str.toString()), findLexicalTerm(str.toString()), str.toString());
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
								if(level < 0) {
									System.out.println("Too much ) ");
									System.exit(2);
								}
								return new LexicalTerm(new Character(car).toString(), new Character(car).toString(), new Character(car).toString());
							}
							next = new LexicalTerm(new Character(car).toString(), new Character(car).toString(), new Character(car).toString());
							LexicalTerm t = new LexicalTerm(findTerm(str.toString()), findLexicalTerm(str.toString()), str.toString());
							if(car == '(') {							
								level++;
							}
							if(car == ')') {
								level--;
							}
							if(level < 0) {
								System.out.println("Too much ) ");
								System.exit(2);
							}
							return t;
						}
					}
				}
				if(ca==']'){
					comment = false;
					
				}
			}
			catch(IOException e) {
				e.printStackTrace();
				hasNext = false;
				return null;
			}
		}
	}




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
		return "l_id";
	}

	private String findLexicalTerm(String value) {
		if(value.contains(".")) {
			String[] s = value.split("[.]");
			if(s.length != 2) {
				System.out.println("erreur de syntaxe at " + value);
				System.exit(1);
			}

			return WordIdentifier.getDotExpr(s[0], s[1]);
		}


		String rWord = WordIdentifier.isReservedWord(value);
		if(rWord != null) {
			//System.out.println(rWord);
			if(WordIdentifier.isBinaryOp(rWord)) {
				return LexicalTerm.BINARY_OP;
			}
			if(WordIdentifier.isUnaryOP(rWord)) {
				return LexicalTerm.UNARY_OP;
			}

			return rWord;
		}
		else {
			try {
				Integer.parseInt(value);
				return "number";


			}
			catch(NumberFormatException e) {
				return "id";
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {

		LexicalParser p = new LexicalParser("test1");

		for(Term t : p) {
			LexicalTerm t1 = (LexicalTerm) t;
			if(t != null) {
				System.out.println(t.getValue() + "\t\t" + t.getType() + "\t" + t1.lexicalTerm);

			}
		}
	}
}
