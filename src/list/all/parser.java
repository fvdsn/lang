package list.all;
import java.util.LinkedList;
import list.all.token;

public class parser {

	LinkedList<token> list;
	String program;
	char programchar[];
	String reservedwords[];
	String reservedcharacters[];
	String unary[];
	String binary[];
	


	public  parser(String str){
		this.list = new LinkedList<token>();
		this.program = str;
		programchar = str.toCharArray();
		this.reservedwords = new String[] {"set","if","while","write","fun","method" ,
				"null","true","false","read",
				"this","return","skip"};
		this.reservedcharacters = new String[]{"(",")","."};
		this.unary = new String[]{"!","neg","new"};
		this.binary = new String[]{"+","-","*","/","%","|","&","<",">","<=",">="};

	}



	public void addToList(token t){
		this.list.add(t);
	}

	public void endOfCurrent(StringBuffer str){
		if(str.length()==0){
			return;
		}

		for(int i=0;i<reservedwords.length;i++){
			if(str.toString().equals(reservedwords[i])){
				this.list.add(new token(str.toString(),reservedwords[i]));
				return;
			}
		}
		
		for(int i=0;i<unary.length;i++){
			if(str.toString().equals(unary[i])){
				this.list.add(new token(str.toString(),"unary"));
				return;
			}
		}
		
		for(int i=0;i<binary.length;i++){
			if(str.toString().equals(binary[i])){
				this.list.add(new token(str.toString(),"binary"));
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
				this.list.add(new token(str.toString(),"number"));
			}
			else{
				this.list.add(new token(str.toString(),"pnumber"));
			}
		}else{
			this.list.add(new token(str.toString(),"id"));
		}
	}

	public LinkedList getList(){
		char newchar;
		StringBuffer current = new StringBuffer("");
		boolean virer=false;

		for(int i=0;i<programchar.length;i++){
			newchar = programchar[i];

			if(newchar==']'){
				virer = false;
			}else{
				if(!virer){

					if(newchar=='['){
						virer = true;
					}else if(newchar=='('){
						this.list.add(new token("(","("));
					}else if(newchar==')'){
						endOfCurrent(current);
						current = new StringBuffer("");
						this.list.add(new token(")",")"));
					}else if(newchar=='.'){
						endOfCurrent(current);
						current = new StringBuffer("");
						this.list.add(new token(".","."));
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

}
