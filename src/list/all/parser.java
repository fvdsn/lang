package list.all;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * @author Bernard Van De Walle
 *
 */
public class parser {

	LinkedList<Token> list;
	String program;
	char programchar[];
	String reservedwords[];
	String reservedcharacters[];
	String unary[];
	String binary[];
	ArrayList<Character> character;



	/**
	 * @param str : Representation du programme.
	 * post : Ce constructeur initialise le parseur avec le programme passé en argument. Il est donc possible par la suite d'utiliser cet objet afind e récupérer la linkedlist.g
	 */
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





	/**
	 * @param str : Représentation du buffer a être analysé.
	 * post : Une mesure est prise par rapport à ce que contient le buffer ( un jeton est ajouté a la linkedlist).
	 */
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

	/**
	 * @return La linkedList contenant tout les tokens.
	 * @throws LexicalError
	 * 
	 * Cette methode permet de parser caractère par caractère tout le programme et de renvoyer une linkedlist correspondante.
	 */
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
