package happy.parser.lexical;

import happy.parser.bnf.Term;
import happy.parser.bnf.TermImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ParserIterator implements Iterator{

	private BufferedReader reader;

	private char programchar[];
	private String reservedwords[];
	private String reservedcharacters[];
	private String unary[];
	private String binary[];
	private ArrayList<Character> character;
	private LinkedList<Token>  list;
	
	private boolean end;

	public ParserIterator(String file) throws FileNotFoundException, IOException {
		File f = new File(file);
		this.reader = new BufferedReader(new FileReader(f));


		char cha[] = { '!' , '@' , '#' , '$' , '%' , '^' , '&' , '*' , '{' , '}' , '|' , '_' , ',' , '?' , '-' , '+' , '=' , '/' , '\\' , '>' , '<' , ':' , ';' , '~' , '\'' , '"', 
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z', 
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z', '(', ')', '.', ' ', '\n' , '\t', 
				'0','1','2','3','4','5','6','7','8','9'};
		this.character = new ArrayList<Character>();
		for(char c : cha) {
			this.character.add(c);
		}

		this.reservedwords = new String[] {"set","if","while","write","fun","method" ,
				"null","true","false","read",
				"this","return","skip"};
		this.reservedcharacters = new String[]{"(",")","."};
		this.unary = new String[]{"!","neg","new"};
		this.binary = new String[]{"+","-","*","/","%","|","&","<",">","<=",">="};
		this.list = new LinkedList<Token>();
	}

	public boolean hasNext() {
		return !list.isEmpty();
	}

	public Term getNextTerm(){
		Token tok = (Token)next();
		if(tok==null){
			return null;
		}
		TermImpl term = new TermImpl(tok.type,true);
		term.setValue(tok.name);
		return term;
	}
	
	public Object next() {
		if(end){
			return null;
		}
		if(!list.isEmpty()){
			return list.pop();
		}
		else{
			try {
				addToken();
			} catch (LexicalError e) {
				e.printStackTrace();
				end = true;
				return null;
			}
			return next();
		}
	}

	public char nextChar(){
		
		char nextchar = 0;
		try {
			if((nextchar=(char) reader.read())==-1 ){
				//TODO : End Of File.
				this.end = true;
			}
		} catch (IOException e) {
			System.out.println("IO Error");
		}
		return nextchar;
	}
	
	public void addToken()throws LexicalError {

		char newchar = 0;
		StringBuffer current = new StringBuffer("");
		boolean virer=false;

		while(true){
			
			newchar = nextChar();
			
			if(!this.character.contains(newchar)) {
				throw new LexicalError("=> " + newchar);
			}

			if(newchar==']'){
				virer = false;
			}else{
				if(!virer){

					if(newchar=='['){
						virer = true;
					}else if(newchar=='('){
						this.list.add(new Token("(","("));
						addSpace();
						return;
					}else if(newchar==')'){
						endOfCurrent(current);
						current = new StringBuffer("");
						this.list.add(new Token(")",")"));
						addSpace();
						return;
					}else if(newchar=='.'){
						try{
							endOfCurrent(current);
							current = new StringBuffer("");
							this.list.removeLast();
							this.list.add(new Token(".","."));
							return;
						}
						catch(NoSuchElementException e){
							throw new LexicalError("Erreur : No starting point !");
						}
					}else if(newchar==' '){
						endOfCurrent(current);
						current = new StringBuffer("");
						return;
					}else if(newchar=='\n'){
						endOfCurrent(current);
						current = new StringBuffer("");
						return;
					}else if(newchar=='\t'){
						endOfCurrent(current);
						current = new StringBuffer("");
						return;
					}else{
						current.append(newchar);
					}
				}
			}
		}
	}

	public void remove() {
	}

	public void endOfCurrent(StringBuffer str){
		if(str.length()==0){
			return;
		}

		//Verification de si il s'agit d'un reserved word
		for(int i=0;i<reservedwords.length;i++){
			if(str.toString().equals(reservedwords[i])){
				this.list.add(new Token(str.toString(),reservedwords[i]));
				addSpace();
				return;
			}
		}

		//Verification de si il s'agit d'un unary
		for(int i=0;i<unary.length;i++){
			if(str.toString().equals(unary[i])){
				this.list.add(new Token(str.toString(),"unary"));
				addSpace();
				return;
			}
		}

		//Verification de si il s'agit d'un binary
		for(int i=0;i<binary.length;i++){
			if(str.toString().equals(binary[i])){
				this.list.add(new Token(str.toString(),"binary"));
				addSpace();
				return;
			}
		}


		boolean digit=true;
		boolean neg = true;

		char temp[] = str.toString().toCharArray();

		//Verification de si il s'agit d'un négatif.
		if(!(temp[0]=='-')){
			neg = false;
			if(!(Character.isDigit(temp[0]))){
				digit=false;
			}
		}

		//Verification de si il s'agit d'un nombre
		for(int i=1;i<temp.length;i++){
			if(!(Character.isDigit(temp[i]))){
				digit=false;
			}
		}

		if(digit){
			if(neg){
				this.list.add(new Token(str.toString(),"number"));
				addSpace();
			}
			else{
				this.list.add(new Token(str.toString(),"pnumber"));
				addSpace();
			}
		}else{
			this.list.add(new Token(str.toString(),"id"));
			addSpace();
		}
	}

	private void addSpace() {
		this.list.add(new Token(" ", "esp"));
	}

	private String getNextLine(){
		StringBuilder str = new StringBuilder();
		try{
			String line = reader.readLine();
			str.append(line + "\n");
			return str.toString();
		}
		catch(IOException e){
		}
		return null;
	}
}
