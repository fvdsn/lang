package happy.parser.syntax;

import java.util.ArrayList;
import java.util.List;

import happy.parser.bnf.Term;
import happy.parser.newlexical.LexicalTerm;
import happy.parser.util.CharIdentifier;

public class TreeOrganiser {
	Term tree;
	LexicalTerm lexTree;
	
	public TreeOrganiser(Term tree) {
		this.tree = tree;
	}
	
	public LexicalTerm contract() {
		contract(tree);
		printTree();
		//contract(tree);
		removeLastBlock(tree);
		
		phase2(tree);
		printTree();
		
		check(tree);
		lexTree = new LexicalTerm(tree.getType(), tree.getChildList());
		lexTree.printLexTree(0);
		
		return lexTree;
		
		
	}
	
	private void check(Term t) {
		
		System.out.println(t.getType());
		char last = t.getType().charAt(t.getType().length() - 1);
		if(last == 't') {
			System.out.println("Erreur de syntaxe");
			System.exit(0);
		}
		for(Term tt : t.getChildList()){
			check(tt);
		}
	}
	
	private void contract(Term tree) {
		List<Term> list = tree.getChildList();
		for(int i = 0; i < list.size(); i++) {
			Term t = list.get(i);
			//c'est un id
			System.out.println(t.getType());
			char last = t.getType().charAt(t.getType().length() - 1);
			//if(CharIdentifier.isDigit(last) && t.getChildList().size() == 1 ) {
				//list.set(i, t.getChildList().get(0));
				//System.out.println("c'est un id on reduit");
				
			//}
			
			if(last == 'k') {
				System.out.println("on a un block");
			}
			
			if(last == 't') {
				System.out.println("on a une liste");
				List<Term> child = t.getChildList();
				if(child.size() == 2) { // toujours une liste
					System.out.println(">>>>>>>");
					Term tt = child.remove(1);
					for(Term ttt : tt.getChildList()) {
						child.add(ttt);
					}
				}
				else if (child.size() == 1){
					t = child.get(0);
					list.set(i, child.get(0));
				}
			}
			
			last = t.getType().charAt(t.getType().length() - 1);
			
			if(CharIdentifier.isDigit(last)) {
				List<Term> child = t.getChildList();
				if(child.size() == 1) {
					list.set(i, t.getChildList().get(0));
					//System.out.println("c'est un id on reduit");
				}
				if(child.size() == 2) {
					
					Term temp = child.remove(1);
					List<Term> all = child.remove(0).getChildList();
					for(Term tt : all) {
						child.add(tt);
					}
					child.add(temp);
				}
				
			}
			
			contract(t);
		}
	}
	
	public void removeLastBlock(Term tree) {
		List<Term> list = tree.getChildList();
		Term temp = list.remove(1);
		List<Term> child = list.remove(0).getChildList();
		for(Term tt : child) {
			list.add(tt);
		}
		list.add(temp);
	}
	
	public void phase2(Term tree) {
		List<Term> list = tree.getChildList();
		for(int i = 0; i < list.size(); i++) {
			Term t = list.get(i);
			
			char last = t.getType().charAt(t.getType().length() - 1);
			
			phase2(t);
			
			if(last == 't') {
				if(i == 1) {
					Term temp = list.remove(i+1);
					List<Term> child = list.remove(i).getChildList();
					System.out.println(t.getType());
					System.out.println("on a une liste");
					for(Term tt : child) {
						list.add(tt);
					}
					list.add(temp);
				}
			}
			
			
			
			
		}
	}
	
	
	public void printTree() {
		tree.printTree(0);
	}
	
	
}