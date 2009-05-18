package happy.parser.lexical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class CodeLoader {
	private String s;
	
	public CodeLoader(String file) throws FileNotFoundException, IOException {
		StringBuilder str = new StringBuilder();
		File f = new File(file);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = reader.readLine();
		while(line != null) {			
			str.append(line + "\n");			
			line = reader.readLine();
		}
		
		s = str.toString();
	}
	
	public String getRep() {
		return s;
	}
	/*
	public static void main(String[] args) throws Exception {
		CodeLoader load = new CodeLoader("test.sm");
		System.out.println(load.getRep());
		LexicalParser p = new LexicalParser(load.getRep());
		LinkedList<Token> l = p.getList();
		for(int i=0;i<l.size();i++){
			System.out.println("token "+i+" : "+l.get(i).name+"\t"+l.get(i).type+"\n");
		}
		
	}*/
}
