package happy.parser.syntax;

import happy.checker.CheckPrecedence;
import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;
import happy.parser.bnf.TermImpl;
import happy.parser.lexical.LexicalParser;
import happy.parser.lexical.ParserIterator;

import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class SyntaxParser {
	private Stack<Term> stack = null;
	private ParserIterator lexParser = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = null ;
	private List<Rule> grammar = null;
	private Term tree = null;
	public SyntaxParser(ParserIterator lexParser, List<Rule> grammar, 
			Hashtable<Term,Hashtable<Term,String>> prectable ){
		this.grammar   = grammar;
		this.lexParser = lexParser;
		this.prectable = prectable;
		this.stack = new Stack<Term>();
	}
	public boolean shift(){
		Term next = lexParser.getNextTerm();
		if(next != null){
			stack.push(next);
			System.out.println("Token : "+ next.getValue()+":"+next.toString());
			return true;
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
		}
		System.out.println("");
	}
	public boolean reduce(){
		boolean hasReduced = false;
		for(Rule r:grammar){
			for (CatList c:r.getOrList()){
				List<Term> l = c.getTermList();
				int llen = l.size();
				int slen = stack.size();
				int start = slen - llen;
				int i = 0;
				boolean match = true;
				if(slen >= llen){
					while(i < llen){	/* check if the top of the stack matches a right side */
						if(!stack.get(start + i).equals(l.get(start + i))){
							match = false;
							break;
						}
						i++;
					}
					if(match){
						hasReduced = true;
						/* we create a new Term R with the matched ones as childs */
						Term R = new TermImpl(r.getLeftSide().toString(),false);
						List<Term> Rchild = R.getChildList();
						i = 0;
						while(i <llen){
							Rchild.add(stack.get(start + i));
							i++;
						}
						/* we remove the matched ones from the stack */
						i = 0;
						while(i < llen){
							stack.pop();
							i++;
						}
						/* we add R on top of the stack */
						stack.push(R);
					}
				}
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
					System.out.println("illegal grammar");
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
