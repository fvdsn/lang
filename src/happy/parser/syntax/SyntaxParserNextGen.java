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
	/*
	 * @pre :~
	 * @post: A terminal is read from the lexical parser and put ont
	 * the top of the stack, and returns true.
	 * If there is no terminal left to read, it will do nothing and return 
	 * false
	 */
	public boolean shift(){
		if(next == null && lexParser.hasNext()){
			System.out.println("coucou");
			stack.push(lexParser.next());
			if(lexParser.hasNext()){
				next = lexParser.next();
			}
			return true;
		}else if(next != null){
			stack.push(next);
			next = lexParser.next();
			return true;
		}
		return false;
	}
	/*
	 * @pre : There is a symbol on the stack.
	 * @post : ~
	 * @returns : 
	 * Returns the precedence relation between the symbol with index
	 * i and i+1 on the stack. If i is equal to the stack size -1 it
	 * returns the precedence relation with the symbol that will be
	 * placed on the stack on the next shift.
	 */
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
	/*
	 * @pre : ~
	 * @post : Prints the stack and the next element. 
	 */
	public void printStack(){
		int i = 0;
		int size = stack.size();
		while(i < size){
			System.out.print(stack.get(i).getType());
			if(i < stack.size()){
				System.out.print(prec(i));
			}
			i++;
		}
		if(next == null){
			System.out.println("  | END");
		}else{
			System.out.println("  | "+next.getType());
		}
	}
	/* 
	 * @pre: ~
	 * @post: ~
	 * @return: true if there is only the start symbol left on the stack, and 
	 * nothing left to read on the lexical parser.
	 */
	public boolean done(){
		if(stack.size() == 1 && next == null){
			for(Rule r:grammar){
				if(r.isStart() && 
						stack.get(0).getType().equals(r.getLeftSide().getType())){
					return true;
				}
			}
		}
		return false;
	}
	/*
	 * @pre : There is a symbol on the stack.
	 * @post :
	 * Tries to match the stop of the stack with the right side of a rule.
	 * It will try to match <.= ... > handles first. If it cannot match
	 * the first <. ... .> handle found, it will stop and return false.
	 * If it finds a match the handle is removed from the stack, put as
	 * the child of a new Term corresponding to the left hand of the matching
	 * rule, and that Term is put on the top of the stack.
	 * @return : returns true if could reduce, false if it couldn't. 
	 */
	public boolean reduce(){
		int i = stack.size() -1; /* beginning of the handle */
		boolean hard = false;	/*it has tried to match a < ... > handle */
		boolean match = true;	
		while(i >= 0){
			int matchsize = stack.size()-i;
			if( 	prec(i-1).equals(CheckPrecedence.LE)
				||	prec(i-1).equals(CheckPrecedence.LEQ)){
				if(prec(i-1).equals(CheckPrecedence.LE)){
					hard = true;
				}
				for(Rule r:grammar){
					for(CatList c:r.getOrList()){	/*we iterate over rules */
						List<Term> L = c.getTermList();
						if(L.size() != matchsize){
							continue;
						}
						int j = 0;
						match = true;
						while(j < matchsize){	/*matching the handle with the rule*/
							if(!L.get(j).equals(stack.get(i+j))){
								match = false;
								break;
							}
							j++;
						}
						if(!match){
							continue;
						}
						Term R = new TermImpl(r.getLeftSide().getType(),false);
						j = 0;
						while(j < matchsize){ /*removing from the stack and adding to the child list */
							R.getChildList().add(stack.remove(i));
							j++;
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
	/*
	 * @pre : the object has been correctly initialized with a lexical
	 * parser and a precedence table.
	 * @post : Tries to parse the input, if successful puts the result in tree and
	 * returns true. returns false and prints an error message otherwise. 
	 */
	public boolean parse(){
		while(shift()){
			while(prec(stack.size()).equals(CheckPrecedence.GE)){
				if(!reduce()){
					if (done()){
						tree = stack.get(0);
						return true;
					}
					System.out.println("Syntax Error : Could not reduce");
					printStack();
					return false;
				}
				if(done()){
					tree = stack.get(0);
					return true;
				}
			}
		}
		if (done()){
			tree = stack.get(0);
			return true;
		}else{
			System.out.println("Syntax Error : Program too long");
			return false;
		}
	}
	/*
	 * @pre:~
	 * @return: returns the latest valid syntax tree. 
	 */
	public Term getParsedTree(){
		return tree;
	}
}
