package list.all;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class parser {

	LinkedList<Token> list;
	String program;
	char programchar[];
	String reservedwords[];
	String reservedcharacters[];
	String unary[];
	String binary[];
	ArrayList<Character> character;



	public  parser(String str){
		char cha[] = { '!' , '@' , '#' , '$' , '%' , '^' , '&' , '*' , '{' , '}' , '|' , '_' , ',' , '?' , '-' , '+' , '=' , '/' , '\\' , '>' , '<' , ':' , ';' , '~' , '\'' , '"', 
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z', 
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z', '(', ')', '.', ' ', '\n' , '\t', 
				'0','1','2','3','4','5','6','7','8','9'};
		character = new ArrayList<Character>();
		for(char c : cha) {
			character.add(c);
		}


		this.list = new LinkedList<Token>();
		this.program = str;
		programchar = str.toCharArray();
		this.reservedwords = new String[] {"set","if","while","write","fun","method" ,
				"null","true","false","read",
				"this","return","skip"};
		this.reservedcharacters = new String[]{"(",")","."};
		this.unary = new String[]{"!","neg","new"};
		this.binary = new String[]{"+","-","*","/","%","|","&","<",">","<=",">="};



	}





	public void endOfCurrent(StringBuffer str){
		if(str.length()==0){
			return;
		}

		for(int i=0;i<reservedwords.length;i++){
			if(str.toString().equals(reservedwords[i])){
				this.list.add(new Token(str.toString(),reservedwords[i]));
				addSpace();
				return;
			}
		}

		for(int i=0;i<unary.length;i++){
			if(str.toString().equals(unary[i])){
				this.list.add(new Token(str.toString(),"unary"));
				addSpace();
				return;
			}
		}

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

		if(!(temp[0]=='-')){
			neg = false;
			if(!(Character.isDigit(temp[0]))){
				digit=false;
			}
		}

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

	public LinkedList<Token> getList() throws LexicalError {
		char newchar;
		StringBuffer current = new StringBuffer("");
		boolean virer=false;

		for(int i=0;i<programchar.length;i++){
			newchar = programchar[i];
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
					}else if(newchar==')'){
						endOfCurrent(current);
						current = new StringBuffer("");
						this.list.add(new Token(")",")"));
						addSpace();
					}else if(newchar=='.'){
						try{
							endOfCurrent(current);
							current = new StringBuffer("");
							this.list.removeLast();
							this.list.add(new Token(".","."));
						}
						catch(NoSuchElementException e){
							throw new LexicalError("Erreur : No starting point !");
						}
					}else if(newchar==' '){
						endOfCurrent(current);
						current = new StringBuffer("");
					}else if(newchar=='\n'){
						endOfCurrent(current);
						current = new StringBuffer("");
					}else if(newchar=='\t'){
						endOfCurrent(current);
						current = new StringBuffer("");
					}else{
						current.append(newchar);
					}
				}
			}
		}

		return this.list;
	}

	private void addSpace() {
		this.list.add(new Token(" ", "esp"));
	}

}
