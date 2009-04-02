package list.all;

import java.util.LinkedList;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "(fun test.dsf. 123 prout . yop -123 123 +123 xzv-123  )";
		System.out.println(str);
		parser p = new parser(str);
		LinkedList<token> l = p.getList();
		for(int i=0;i<l.size();i++){
			System.out.println("token "+i+" : "+l.get(i).name+"\t"+l.get(i).type+"\n");
		}
	}

}
