package happy.parser.newlexical;


import happy.parser.bnf.Term;
import happy.parser.syntax.AnonymousCounter;
import happy.parser.util.WordIdentifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import slip.internal.Prog;
import slip.interpreter.Interpreter;
import slip.parser.Cand;
import slip.parser.Cass;
import slip.parser.Ccmd;
import slip.parser.Ccond;
import slip.parser.Ci;
import slip.parser.Cinput;
import slip.parser.Clexpr;
import slip.parser.Cmethod;
import slip.parser.Cnot;
import slip.parser.Cor;
import slip.parser.Coutput;
import slip.parser.Cprog;
import slip.parser.Creturn;
import slip.parser.Crexpr;
import slip.parser.Cseq;
import slip.parser.CsimpleCall;
import slip.parser.Cvar;
import slip.translation.MethodTable;


public class Translator {
	
	public Translator(LexicalTerm tree) {
		LinkedList<Cmethod> methods = new LinkedList<Cmethod>();
		for(LexicalTerm t : tree.getLexChildList()) {
			if(!t.isTerminal()) {
				methods.add(analyseMethod(t));
			}
		}
		Cmethod[] m = new Cmethod[1];
		m = methods.toArray(m);
		try {
			
			Cprog prog = new Cprog(m);
			Prog p = prog.translate();
			Interpreter.pro = p;
			System.out.println(prog);
			System.out.println(p);
			p.execute();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("erreur");
		}
	}
	
	public Cmethod analyseMethod(LexicalTerm t) {
		removeBrackets(t);
		List<LexicalTerm> child = t.getLexChildList();
		
		for(int i = child.size() -1 ; i >= 0; i--) {
			LexicalTerm tt = child.get(i);
			if(tt.lexicalTerm == null) {
				System.out.println("list");
			}
			else if(tt.lexicalTerm.equals("fun")) {
				System.out.println("fun");
				return addMethod(child.get(i + 1), child.get(i + 2), -1);
			}
			else if(tt.lexicalTerm.equals("method_int")) {
				System.out.println("method");
				return addMethod(child.get(i + 1), child.get(i + 2), WordIdentifier.getLevel(tt.getValue()));
				
			}
			else {
				System.out.println("Erreur de syntaxe 1");
				System.exit(0);
			}
			
		}
		return null;
		
	}
	
	public Cmethod addMethod(LexicalTerm args, LexicalTerm cmd, int level) {
		removeBrackets(args);
		removeBrackets(cmd);
		//System.out.println(args);
		//System.out.println(cmd);
		int i = -1;
		int nbArg = args.getLexChildList().size() - 1;
		String[] arg = new String[nbArg];
		String name = "";
		MethodTable table = new MethodTable(nbArg);
		for(LexicalTerm t : args.getLexChildList()) {
			//System.out.println(t);
			if(t.lexicalTerm == null || !t.lexicalTerm.equals("id")) {
				System.out.println("erreur de syntaxe 2");
				System.exit(-1);
			}
			if(i == -1) {
				name = t.getValue();
			}
			else {
				try {
					table.addFormal(t.getValue());
				}
				catch(Exception e) {
					System.out.println("PS exception de type Exception non documenté c'est horrible");
				}
				arg[i] = t.getValue();
			}
			i++;
		}
		
		return new Cmethod(name, level, arg, addBody(cmd, table), table);
		
		
	}
	
	private Cseq addBody(LexicalTerm cmd, MethodTable table ) {
		AnonymousCounter counter = new AnonymousCounter();
		ArrayList<Ccmd> seq = new ArrayList<Ccmd>();
		for(LexicalTerm t : cmd.getLexChildList()) {
			exploreCmd(t, table, seq,counter);
		}
		Ccmd[] t =  new Ccmd[seq.size()];
		for(int i = 0; i < seq.size(); i++) {
			t[i] = seq.get(i);
		}
		
		return new Cseq(t);
	}
	
	private Cvar exploreCmd(LexicalTerm cmd, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		removeBrackets(cmd);
		List<LexicalTerm> child = cmd.getLexChildList();
		
		if(!child.get(0).isTerminal()) {
			System.out.println("Erreur de syntaxe expected id or operator");
			System.exit(12);
		}
		int id = identifier(child.get(0), child.size());
		if(id == -1) {
			System.exit(14);
		}
		switch(id) {
		case INPUT:
			return addInput(child, table, body, counter);
		case ASS: 
			return addAssignement(child, table, body, counter);
		case OUTPUT:
			return addOutput(child, table, body, counter);
		case RETURN:
			return addReturn(child, table, body, counter);		
		case SIMPLE_CALL:
			return addSimpleCall(child, table, body, counter);
		case ARITHMETIC_OP:
			return addArithmeticOp(child, table, body, counter);
		case IF:
			return addIf(child, table, body, counter);
		case IF_ELSE:
			return addIf(child, table, body, counter);
		}
		
			
		
		
		return null;
	}
	
	
	
	private Cvar addInput(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		String name = counter.getAnonymousName();
		Cvar var = new Cvar(name);
		Clexpr[] expr = {var};
		table.addLocal(name);
		Cinput input = new Cinput(expr);
		body.add(input);
		return var;
	}
	
	private Cvar addAssignement(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(!child.get(1).isTerminal() || !child.get(1).lexicalTerm.equals("id")) {
			System.out.println("Expected id after a set");
		}
		Cvar v1 = new Cvar(child.get(1).getValue());
		table.addLocal(child.get(1).getValue());
		Cass ass;
		if(child.get(2).isTerminal()) {
			if(WordIdentifier.isRexpr(child.get(2))) {
				Crexpr r = WordIdentifier.getRexpr(child.get(2)); 
				ass = new Cass(v1,r );
				body.add(ass);
			}
			else if(child.get(2).lexicalTerm.equals("id")) {
				Cvar v2 = new Cvar(child.get(2).getValue());
				table.addLocal(child.get(2).getValue());
				
				ass = new Cass(v1,v2 );
				body.add(ass);
			}
			else {
				System.out.println("Expected number id or expr");
				System.exit(15);
			}			
		}
		else {
			Cvar v2 = exploreCmd(child.get(2), table, body, counter);
			ass = new Cass(v1,v2);
			body.add(ass);
		}
		return v1;
	}
	
	private Cvar addOutput(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(1).isTerminal()) {
			Cvar v1 = null;
			if(WordIdentifier.isRexpr(child.get(1))) {
				Crexpr r = WordIdentifier.getRexpr(child.get(1)); 
				String name = counter.getAnonymousName();
				table.addLocal(name);
				v1 = new Cvar(name);				
				body.add(new Cass(v1,r ));
				
				
			}
			else if(child.get(1).lexicalTerm.equals("id")) {
				v1 = new Cvar(child.get(1).getValue());
				table.addLocal(child.get(1).getValue());
			}
			
			else {
				System.out.println("Expected number id or expr");
				System.out.println("in write");
				System.exit(15);
			}	
			Clexpr[] expr = {v1};
			Coutput out = new Coutput(expr);
			body.add(out);
			return v1;
		}
		else {
			Cvar v1 = exploreCmd(child.get(1), table, body, counter);
			Clexpr[] expr = {v1};
			Coutput out = new Coutput(expr);
			body.add(out);
			return v1;
		}

	}
	
	private Cvar addReturn(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(1).isTerminal()) {
			Cvar v1 = null;
			if(WordIdentifier.isRexpr(child.get(1))) {
				Crexpr r = WordIdentifier.getRexpr(child.get(1)); 
				String name = counter.getAnonymousName();
				table.addLocal(name);
				v1 = new Cvar(name);				
				body.add(new Cass(v1,r ));
				
				
			}
			else if(child.get(1).lexicalTerm.equals("id")) {
				v1 = new Cvar(child.get(1).getValue());
				table.addLocal(child.get(1).getValue());
			}
			
			else {
				System.out.println("Expected number id or expr");
				System.exit(15);
			}	
			
			
			body.add(new Creturn(v1));
			return v1;
		}
		else {
			Cvar v1 = exploreCmd(child.get(1), table, body, counter);
			body.add(new Creturn(v1));
			return v1;
		}
	}
	
	private Cvar addSimpleCall(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		String nom = child.get(0).getValue();
		Crexpr[] args = new Crexpr[child.size() - 1];
		for(int i = 1; i < child.size(); i++) {
			if(child.get(i).isTerminal()) {
				Cvar v1 = null;
				if(WordIdentifier.isRexpr(child.get(i))) {
					Crexpr r = WordIdentifier.getRexpr(child.get(i)); 
					String name = counter.getAnonymousName();
					table.addLocal(name);
					v1 = new Cvar(name);				
					body.add(new Cass(v1,r ));
					args[i - 1] = v1; 
					
					
				}
				else if(child.get(1).lexicalTerm.equals("id")) {
					v1 = new Cvar(child.get(i).getValue());
					table.addLocal(child.get(i).getValue());
					args[i - 1] = v1; 
				}
				
				else {
					System.out.println("Expected number id or expr");
					System.exit(15);
				}	
				
				
				
			}
			else {
				Cvar v1 = exploreCmd(child.get(1), table, body, counter);
				args[i - 1] = v1; 
				
			}
		}
		String name = counter.getAnonymousName();
		table.addLocal(name);
		Cvar v2 = new Cvar(name);				
		body.add(new Cass(v2, new CsimpleCall(nom, args)));
		return v2;
	}
	
	private Cvar addArithmeticOp(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		Cvar[] v1 = new Cvar[2];
		
		for(int i = 1; i < child.size(); i++) {
			
			if(child.get(i).isTerminal()) {
				if(WordIdentifier.isRexpr(child.get(i))) {
					Crexpr r = WordIdentifier.getRexpr(child.get(i)); 
					String name = counter.getAnonymousName();
					Cvar v = new Cvar(name);
					table.addLocal(name);
					Cass ass = new Cass(v,r );
					body.add(ass);
					v1[i - 1] = v;
				}
				else if(child.get(i).lexicalTerm.equals("id")) {
					Cvar v = new Cvar(child.get(i).getValue());
					table.addLocal(child.get(i).getValue());
					v1[i - 1] = v;
				}
				else {
					System.out.println("Expected number id or expr");
					System.exit(15);
				}			
			}
			else {
				Cvar v = exploreCmd(child.get(i), table, body, counter);
				v1[i - 1] = v;
			}
		}
		
		String name = counter.getAnonymousName();
		table.addLocal(name);
		Cvar v2 = new Cvar(name);				
		body.add(new Cass(v2, WordIdentifier.getBinary(child.get(0), v1[0], v1[1])));
		
		return v2;
	}
	
	private Cvar addIf(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		
		Ccond cond = getCond(child.get(1), table, body, counter);
		Ccmd b = getInnerBody(child.get(2), table, counter);
		
		
		
		//pour la compatibilité on renvoie rezo.
		Crexpr r = new Ci(0);
		String name = counter.getAnonymousName();
		Cvar v = new Cvar(name);
		table.addLocal(name);
		Cass ass = new Cass(v,r );
		body.add(ass);
		return v;
	}
	
	private Cvar addIfElse(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		
		Ccond cond = getCond(child.get(1), table, body, counter);
		Ccmd b = getInnerBody(child.get(2), table, counter);
		Ccmd c = getInnerBody(child.get(3), table, counter);
		
		
		
		
		//pour la compatibilité on renvoie rezo.
		Crexpr r = new Ci(0);
		String name = counter.getAnonymousName();
		Cvar v = new Cvar(name);
		table.addLocal(name);
		Cass ass = new Cass(v,r );
		body.add(ass);
		return v;
	}
	
	
	private Cseq getInnerBody(LexicalTerm cmd, MethodTable table, AnonymousCounter counter) {
		
		ArrayList<Ccmd> body = new ArrayList<Ccmd>();
		
		exploreCmd(cmd, table, body,counter);
		
		Ccmd[] t =  new Ccmd[body.size()];
		for(int i = 0; i < body.size(); i++) {
			t[i] = body.get(i);
		}
		
		return new Cseq(t);
	}
	
	
	private void removeBrackets(LexicalTerm t) {
		LexicalTerm t1 = t.getLexChildList().remove(0);
		LexicalTerm t2 = t.getLexChildList().remove(t.getLexChildList().size() -1);
	}
	
	private int identifier(LexicalTerm t, int size) {
		String term = t.lexicalTerm;
		
		if(term.equals("read")) {
			if(size != 1) {
				System.out.println("No args expected for input");
				return ERROR;
			}
			return INPUT;
		}
		if(term.equals("write")) {
			if(size != 2) {
				System.out.println("only one args for output");
				return ERROR;
			}
			return OUTPUT;
		}
		if(term.equals("while")) {
			if(size != 3) {
				System.out.println("Expected cond and seq after a while");
				return ERROR;
			}
			return WHILE;
		}
		if(term.equals("if")) {
			if(size == 3) {
				return IF_ELSE;
				
			}
			if(size == 2) {
				return IF;
			}
			else {
				System.out.println("Expected cond and seq or cond seq seq for if");
				return ERROR;
			}
		}
		if(term.equals("set")) {
			if(size != 3) {
				System.out.println("Expected id and expression for a set");
				return ERROR;
			}
			return ASS;
		}
		if(term.equals("return")) {
			if(size != 2) {
				System.out.println("Expected id and expression for a set");
				return ERROR;
			}
			return RETURN;
		}
		if(term.equals("id")) {
			return SIMPLE_CALL;
		}
		if(term.equals(LexicalTerm.BINARY_OP)) {
			if(WordIdentifier.isBinaryArithmetic(t)) {
				if(size != 3) {
					System.out.println("Expected expression and expression for an arithmetic operator");
					return ERROR;
				}
				return ARITHMETIC_OP;
			}
			//TODO
			return ERROR;
		}
		return ERROR;
		
	}
	
	
	private Ccond getCond(LexicalTerm term, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		removeBrackets(term);
		List<LexicalTerm> child = term.getLexChildList();
		String op = child.get(0).getValue();
		if(op.equals("not")) {
			if(child.size() != 2) {
				System.out.println("Syntax error in condition not");
				System.exit(16);
			}
			return getNot(child, table, body, counter);
		}
		if(op.equals("not")) {
			if(child.size() != 2) {
				System.out.println("Syntax error in condition not");
				System.exit(16);
			}
			return getNot(child, table, body, counter);
		}
		if(op.equals("and")) {
			if(child.size() != 3) {
				System.out.println("Syntax error in condition not");
				System.exit(16);
			}
			return getAnd(child, table, body, counter);
		}
		if(op.equals("or")) {
			if(child.size() != 3) {
				System.out.println("Syntax error in condition not");
				System.exit(16);
			}
			return getOr(child, table, body, counter);
		}
		if(WordIdentifier.isBinaryLog(child.get(0))) {
			System.out.println("oooooook");
		}
		System.out.println(op);
		return null;
	}
	
	private Ccond getNot(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(2).isTerminal()) {
			System.out.println("Syntax error at not, expected condition");
			System.exit(16);
		}
		return new Cnot(getCond(child.get(2), table, body, counter));
		
		
	}
	
	private Ccond getAnd(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(2).isTerminal() || child.get(3).isTerminal()) {
			System.out.println("Syntax error at AND, expected condition");
			System.exit(16);
		}
		return new Cand(getCond(child.get(2), table, body, counter),getCond(child.get(3), table, body, counter));
		
		
	}
	
	private Ccond getOr(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(2).isTerminal() || child.get(3).isTerminal()) {
			System.out.println("Syntax error at AND, expected condition");
			System.exit(16);
		}
		return new Cor(getCond(child.get(2), table, body, counter),getCond(child.get(3), table, body, counter));
		
		
		
	}
	
	public static final int ERROR = -1;
	public static final int INPUT = 0;
	public static final int OUTPUT = 1;
	public static final int WHILE = 2;
	public static final int IF_ELSE = 3;
	public static final int IF = 4;
	public static final int ASS = 5;
	public static final int RETURN = 6;
	public static final int SIMPLE_CALL = 7;
	public static final int ARITHMETIC_OP = 8;
	
	
	
	
}
