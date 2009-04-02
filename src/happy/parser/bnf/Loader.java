package happy.parser.bnf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import happy.checker.*;

public class Loader {
	
	List<Rule> rules;

	public static void main(String[] args) throws Exception {
		Loader load = new Loader("grammar_wp.bnf");
		for(Rule r : load.getRules()) {
			System.out.println(r);
			System.out.println();
		}

		
		System.out.println("Check conflit : ");
		for(RulesTuple r : CheckCycle.checkSuffix(load.getRules())) {
			System.out.println("conflit avec " + r.prob + "\n" + r.r1 + "\n" + r.r2 + " \n");
		}
		
		
		System.out.println("List of all Terms:\n");
		List<Term> l = CheckPrecedence.getAllTerm(load.getRules());
		for(Term t:l){
			System.out.println(t.toString()+":"+t.hashCode());
		}
		
		
		
		

		CheckPrecedence.check(load.getRules());

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
