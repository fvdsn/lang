package happy.parser.syntax;

import happy.checker.CheckPrecedence;
import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;
import happy.parser.bnf.TermImpl;
import happy.parser.newlexical.LexicalParser;
import happy.parser.newlexical.LexicalTerm;


import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class SyntaxParser {
	private Stack<Term> stack = null;
	private LexicalParser lexParser = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = null ;
	private List<Rule> grammar = null;
	private Term tree = null;

	
	public SyntaxParser(LexicalParser lexParser, List<Rule> grammar, 
			Hashtable<Term,Hashtable<Term,String>> prectable ){
		this.grammar   = grammar;
		this.lexParser = lexParser;
		this.prectable = prectable;
		this.stack = new Stack<Term>();
	}
	public boolean shift(){
		if(lexParser.hasNext()) {
			Term next = lexParser.next();
			if(next == null) {
				lastReduce();
				return false;
			}
			else {
		
				stack.push(next);
				//System.out.println("Token : "+ next.getValue()+":"+next.toString());
				return true;
			}
		}else{
			System.out.println("End of Program");
			return false;
		}
	}
	public String prec(int i, int j){
		
		return prectable.get(stack.get(i)).get(stack.get(j));
	}
	public void printStack(){
		int i = 0;
		while(i < stack.size()){
			System.out.print(stack.get(i).toString());
			if(i < stack.size() -1){
				System.out.print(prec(i,i+1));
			}
			i++;
		}
		System.out.println("");
	}
	
	public boolean lastReduce() {

		for(Rule r:grammar) {
			for (CatList c:r.getOrList()){
				List<Term> l = c.getTermList();
				int start = 0;
				boolean match = true;
				for(int i = 0; i < l.size(); i++) {
					if(!stack.get(start + i).getType().equals(l.get(i).getType())) {
						match = false;
						
					}
				}
				System.out.println("LAST MATCHHH");
				Term t = new TermImpl(r.getLeftSide().toString(), false);
				List<Term> Rchild = t.getChildList();
				for(int i = 0; i < l.size(); i++) {
					Rchild.add(stack.get(start + i));
				}
				
				for(int i = 0; i < l.size(); i++) {
					stack.pop();
				}
				stack.push(t);
				//printStack();
				return true;
				
			}
		}
		return false;
	}
	
	public boolean reduce(){
		boolean hasReduced = false;
		for(Rule r:grammar){
			for (CatList c:r.getOrList()){
				List<Term> l = c.getTermList();
				
				
				int start = stack.size() - l.size() - 1;
				if(start >= 0) {
					
					boolean match = true;
					for(int i = 0; i < l.size(); i++) {
						if(!stack.get(start + i).getType().equals(l.get(i).getType())) {
							match = false;
							
						}
					}
					if(match) {
						
						
						Term t = new TermImpl(r.getLeftSide().toString(), false);
						List<Term> Rchild = t.getChildList();
						
						for(int i = 0; i < l.size(); i++) {
							Rchild.add(stack.get(start + i));
						}
						
						Term temp = stack.pop();
						for(int i = 0; i < l.size(); i++) {
							stack.pop();
						}
						stack.push(t);
						stack.push(temp);
						//printStack();
						return true;
					}
					
				}
				
				
			}
		}
		return hasReduced;
		
	}
	public void parse(){
		while(shift()){
			//printStack();
			int s = stack.size();
			if(s >= 2){
				if(prec(s-2,s-1).equals(CheckPrecedence.NOTHING)
						|| prec(s-2,s-1).equals(CheckPrecedence.ERROR)){
					System.out.println("illegal grammar :"+prec(s-2,s-1));
					return;
				}
				
				while(prec(s-2,s-1).equals(CheckPrecedence.GE)){
					if(!reduce()){
						System.out.println("couldn't reduce ...");
						return;
					}
					//printStack();
					s = stack.size();
				}
				
				//System.out.println(prec(s-2,s-1));
				
			}
		}
		if(stack.size() == 1){
			System.out.println("Success");
			tree = stack.pop();
			tree.printTree(0);
			TreeOrganiser to = new TreeOrganiser(tree);
			to.contract();
			to.printTree();
		}else{
			System.out.println("program too long ... ");
		}
	}
	public Term getParsedTree(){
		return tree;
	}

}
