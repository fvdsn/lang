package happy.parser.bnf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loader {
	
	List<Rule> rules;

	public static void main(String[] args) throws Exception {
		Loader load = new Loader("grammar_wp.bnf");
	
		for(Rule r : load.getRules()) {
			System.out.println(r);
			System.out.println();
		}
		
		for(Rule r : load.getRules()) {
			for(CatList l : r.getOrList()) {
				System.out.println(l.stringRep());
			}
		}
	}
	
	
	public Loader(String file) throws FileNotFoundException, IOException {
		rules = new ArrayList<Rule>();
		File f = new File(file);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = reader.readLine();
		while(line != null) {
			try {
				Rule r = new Rule(line);
				this.rules.add(r);
			}
			catch(NotRuleException e) {}
			line = reader.readLine();
		}
	}
	
	public List<Rule> getRules() {
		return rules;
	}
	
	
}
