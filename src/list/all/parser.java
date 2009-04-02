package list.all;
import java.util.LinkedList;

public class parser {

	LinkedList<token> list;
	String program;
	char programchar[];
	String reserved[];


	public  parser(String str){
		this.list = new LinkedList<token>();
		this.program = str;
		programchar = str.toCharArray();
		this.reserved = new String[] {"set","if","while","write","fun","method" ,
				"null","true","false","read","new","+","-","*","/" ,
				"%","|","&","!","<",">","<=",">=","neg",
				"this","return","skip"}; 
	}

	public void addToList(token t){
		this.list.add(t);
	}

	public void endOfCurrent(StringBuffer str){
		if(str.length()==0){
			return;
		}

		for(int i=0;i<reserved.length;i++){
			if(str.toString().equals(reserved[i])){
				this.list.add(new token(str.toString(),reserved[i]));
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
