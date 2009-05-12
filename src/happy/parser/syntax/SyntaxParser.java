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
				System.out.println("Token : "+ next.getValue()+":"+next.toString());
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
				printStack();
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
				
				//System.out.println("--------------");
				int start = stack.size() - l.size() - 1;
				if(start >= 0) {
					//System.out.println(stack.get(start));
					//System.out.println(r.getLeftSide() + "   " + l.get(0));
					//System.out.println("--------------");
					boolean match = true;
					for(int i = 0; i < l.size(); i++) {
						if(!stack.get(start + i).getType().equals(l.get(i).getType())) {
							match = false;
							//System.out.println("NOT MATCH !!!!");
						}
					}
					if(match) {
						System.out.println("MATCHHH");
						//System.out.println(stack.size() - start);
						//System.out.println(stack.size() - start - l.size());
						
						Term t = new TermImpl(r.getLeftSide().toString(), false);
						List<Term> Rchild = t.getChildList();
						//System.out.println(Rchild.size() + " "  + l.size());
						for(int i = 0; i < l.size(); i++) {
							Rchild.add(stack.get(start + i));
						}
						//System.out.println(Rchild.size() + " "  + l.size());
						Term temp = stack.pop();
						for(int i = 0; i < l.size(); i++) {
							stack.pop();
						}
						stack.push(t);
						stack.push(temp);
						printStack();
						return true;
					}
					
				}
				
				/*int llen = l.size();
				int slen = stack.size();
				int start = slen - llen;
				int i = 0;
				System.out.println(llen + "  " + slen + "   " + start);
				boolean match = true;
				if(slen >= llen){
					while(i < llen){	// check if the top of the stack matches a right side 
						System.out.println(stack.get(start + i).toString() + l.get(start + i).toString() );
						if(!stack.get(start + i).equals(l.get(start + i))){
							match = false;
							break;
						}
						i++;
					}
					if(match){
						hasReduced = true;
						// we create a new Term R with the matched ones as childs 
						Term R = new TermImpl(r.getLeftSide().toString(),false);
						List<Term> Rchild = R.getChildList();
						i = 0;
						while(i <llen){
							Rchild.add(stack.get(start + i));
							i++;
						}
						// we remove the matched ones from the stack 
						i = 0;
						while(i < llen){
							stack.pop();
							i++;
						}
						//we add R on top of the stack 
						stack.push(R);
					}
				}*/
			}
		}
		return hasReduced;
		
	}
	public void parse(){
		while(shift()){
			printStack();
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
					printStack();
					s = stack.size();
				}
				
				System.out.println(prec(s-2,s-1));
				boolean red = true;
				while(red) {
					if(prec(s-2,s-1).equals(CheckPrecedence.EQ)) {
						System.out.println("entre");
						if(!reduce()) {
							System.out.println("pas réduit");
							red = false;
						}
						else {
							printStack();
							s = stack.size();
						}
					}
					else {
						red = false;
					}
				}
			}
		}
		if(stack.size() == 1){
			System.out.println("Success");
			tree = stack.pop();
			tree.printTree(0);
		}else{
			System.out.println("program too long ... ");
		}
	}
	public Term getParsedTree(){
		return tree;
	}

}
