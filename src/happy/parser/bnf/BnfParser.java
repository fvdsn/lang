package happy.parser.bnf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import happy.checker.*;

/**
 * This class is a tool to load bnf grammar file, and return the list of the rules.
 * We also put the main method for the programm : checker  
 * 
 *
 */
public class BnfParser {
	
	List<Rule> rules;

	
	/*public static void main(String[] args) throws Exception {
		//load the grammar
		BnfParser load = new BnfParser("grammar_wp_space.bnf");
		//print all the rules
		for(Rule r : load.getRules()) {
			System.out.println(r);
			System.out.println();
		}	
		
		
		
		System.out.println("List of all Terms:\n");
		List<Term> l = CheckPrecedence.getAllTerm(load.getRules());
		for(Term t:l){
			System.out.println(t.toString()+":"+t.hashCode());
		}		
		
		
		//check the grammar
		checkWP.checkAll(load.getRules());

	}*/
	
	
	/**
	 * initialize the rules loader, the file should be a valid BNF File
	 * that mean every rules look like that
	 * nonTerminal ::= rules1 | rules2 | .. | rulesN
	 * nonTerminal should be written like that : <id>
	 * Terminal should be written like that : 'term'
	 * special caracter  ' \ could be in a terminal symbole precede by a \
	 * 
	 * exemple : 
	 * <E> ::= <T> 
	 * <E> ::= '\\' <T> 
	 * <E> ::= <T> '+' <E>
	 * <T> ::= <F>
	 * <T> ::= <F> '*' <T> 
	 * <F> ::= '\''
	 *  
	 * @param file the path of the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public BnfParser(String file) throws FileNotFoundException, IOException {
		rules = new ArrayList<Rule>();
		File f = new File(file);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = reader.readLine();
		boolean first = true;
		while(line != null) {
			try {
				Rule r = new Rule(line);
				if(first){
					System.out.println(r.getLeftSide().getType());
					r.setStart();
				}
				this.rules.add(r);
			}
			catch(NotRuleException e) {}
			line = reader.readLine();
			first = false;
		}
	}
	
	public List<Rule> getRules() {
		return rules;
	}
	
	
}
