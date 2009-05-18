package happy.parser.bnf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BnfGenerator {
	private int level;
	
	public BnfGenerator(int level) {
		this.level = level;
	}
	
	public void Generate(String file) throws IOException {
		File f = new File(file);
		if(f.exists()) {
			f.delete();
		}
		FileWriter writer = new FileWriter(file);
		for(int i = 0; i < level; i++) {
			writer.write(getLi(i) + "\n");
			writer.write(getLiBlock(i) + "\n");
			writer.write(getLiList(i) + "\n");
			
		}
		writer.write(getLn(level) + "\n");
		writer.flush();
		writer.close();
		
	}
	
	private String getLi(int i) {
		return "<l"+ i + "> ::= <l" + i + "_block>  ')' | 'l" + i + "_id'";
	}
	
	private String getLiList(int i) {
		return "<l"+ i + "_list> ::= <l" + (i + 1) + "> | <l" + (i + 1) + "> <l" + i + "_list>";
	}
	
	private String getLn(int n) {
		return "<l"+ n + "> ::= 'l" + n + "_id'";
	}
	
	private String getLiBlock(int i) {
		return "<l"+ i + "_block> ::= '(' <l" + i  + "_list>";
	}
	
	public static void main(String[] args) throws IOException {
		new BnfGenerator(24).Generate("temp.bnf");
	}
	
	
	

}
