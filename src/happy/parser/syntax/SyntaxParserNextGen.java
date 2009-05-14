package happy.parser.syntax;

import happy.checker.CheckPrecedence;
import happy.parser.bnf.CatList;
import happy.parser.bnf.Rule;
import happy.parser.bnf.Term;
import happy.parser.bnf.TermImpl;
import happy.parser.newlexical.LexicalParser;

import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class SyntaxParserNextGen {
	private Stack<Term> stack = null;
	private LexicalParser lexParser = null;
	private Hashtable<Term,Hashtable<Term,String>> prectable = null ;
	private List<Rule> grammar = null;
	private Term tree = null;
	private Term next = null;
	public SyntaxParserNextGen(LexicalParser lexParser, List<Rule> grammar, 
			Hashtable<Term,Hashtable<Term,String>> prectable ){
		this.grammar   = grammar;
		this.lexParser = lexParser;
		this.prectable = prectable;
		this.stack = new Stack<Term>();
	}
	public boolean shift(){
		if(next == null && lexParser.hasNext()){
			stack.push(lexParser.next());
			if(lexParser.hasNext()){
				next = lexParser.next();
			}
			return true;
		}else if(next != null){
			stack.push(next);
			next = lexParser.next();
			return true;
		}else{
			return false;
		}
	}
	public String prec(int i){
		if(i < 0){
			return CheckPrecedence.LE;
		}else if( i >= stack.size() - 1){
			if(next == null){
				return CheckPrecedence.GE;
			}else{
				return prectable.get(stack.lastElement()).get(next);
			}
		}else{
			return prectable.get(stack.get(i)).get(stack.get(i+1));
		}
	}
	public void printStack(){
		int i = 0;
		while(i < stack.size()){
			System.out.print(stack.get(i).toString());
			if(i < stack.size()){
				System.out.print(prec(i));
			}
			i++;
		}
		if(next == null){
			System.out.println("| END");
		}else{
			System.out.println("| "+next.toString());
		}
	}
	public boolean reduce(){
		int i = stack.size() -2;
		boolean hard = false;
		while(i >= 0){
			int matchsize = stack.size()-i;
			if( 	prec(i-1).equals(CheckPrecedence.LE)
				||	prec(i-1).equals(CheckPrecedence.LEQ)){
				if(prec(i-1).equals(CheckPrecedence.LE)){
					hard = true;
				}
				for(Rule r:grammar){
					for(CatList c:r.getOrList()){
						List<Term> L = c.getTermList();
						if(L.size() != matchsize){
							continue;
						}
						int j = 0;
						while(j < matchsize){
							if(!L.get(j).equals(stack.get(i+j))){
								continue;
							}
							j++;
						}
						Term R = new TermImpl(r.getLeftSide().getType(),false);
						j = 0;
						while(j < matchsize){
							R.getChildList().add(stack.remove(i));
						}
						stack.push(R);
						return true;
					}
				}
			}
			if(hard){
				return false;
			}
			i--;
		}
		return false;
	}
	public void parse(){
		while(shift()){
			System.out.println("Shift:");
			printStack();
			while(prec(stack.size()).equals(CheckPrecedence.GE)){
				if(!reduce()){
					if (stack.size() == 1 && !shift()){
						System.out.println("Syntax successfully parsed");
						tree = stack.get(0);
					}
					System.out.println("Syntax Error : Could not reduce");
					return;
				}
				System.out.println("Reduce:");
				printStack();
			}
		}
		if (stack.size() == 1){
			tree = stack.get(0);
		}else{
			System.out.println("Syntax Error : Program too long");
		}
	}
	public Term getParsedTree(){
		return tree;
	}
}
