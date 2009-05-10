package happy.parser.lexical;

import java.util.LinkedList;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String str = ".(fun (test A B)((return 3) ))";
		System.out.println(str);
		LexicalParser p = new LexicalParser(str);
		LinkedList<Token> l = p.getList();
		for(int i=0;i<l.size();i++){
			System.out.println("token "+i+" : "+l.get(i).name+"\t"+l.get(i).type+"\n");
		}
	}

}
