package happy.parser.syntax;



import happy.parser.newlexical.LexicalTerm;
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
import slip.parser.Cfield;
import slip.parser.Ci;
import slip.parser.Cif;
import slip.parser.Cinput;
import slip.parser.Clexpr;
import slip.parser.Cmethod;
import slip.parser.Cminus;
import slip.parser.Cnew;
import slip.parser.Cnot;
import slip.parser.Cor;
import slip.parser.Coutput;
import slip.parser.Cprog;
import slip.parser.CsuperCall;
import slip.parser.Cthis;
import slip.parser.CvariableCall;

import slip.parser.Creturn;
import slip.parser.Crexpr;
import slip.parser.Cseq;
import slip.parser.CsimpleCall;
import slip.parser.CthisField;
import slip.parser.Cvar;
import slip.parser.Cwhile;
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
			System.out.println(prog.toString());
			System.out.println(p.toString());
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
			if(tt.getLexicalTerm() == null) {
				
			}
			else if(tt.getLexicalTerm().equals("fun")) {
				
				return addMethod(child.get(i + 1), child.get(i + 2), -1);
			}
			else if(tt.getLexicalTerm().equals("method_int")) {			
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
		
		int i = -1;
		int nbArg = args.getLexChildList().size() - 1;
		String[] arg = new String[nbArg];
		String name = "";
		MethodTable table = new MethodTable(nbArg);
		for(LexicalTerm t : args.getLexChildList()) {
			
			if(t.getLexicalTerm() == null || !t.getLexicalTerm().equals("id")) {
				System.out.println("erreur de syntaxe 2 : expected id in fun args declaration");
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
					e.printStackTrace();
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
		
	
			exploreCmd(cmd, table, seq,counter);
		
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
			
			if(child.get(0).getLexChildList().get(0).getValue().equals("(")) {
				
				
				for(LexicalTerm t : child) {
					for(Ccmd c : getInnerBody(t, table, counter).cmd) {
						body.add(c);
					}
				}
				
				Crexpr r = new Ci(0);
				String name = counter.getAnonymousName();
				Cvar v = new Cvar(name);
				table.addLocal(name);
				Cass ass = new Cass(v,r );
				body.add(ass);
				return v;
				
			}
			else {
				
				System.out.println("Erreur de syntaxe expected id or operator to start a function");
				System.exit(12);
			}
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
			return addIfElse(child, table, body, counter);
		case WHILE :
			return addWhile(child, table, body, counter);
		case NEW :
			return addNew(child, table, body, counter);
		case NEG :
			return addNeg(child, table, body, counter);
		case ID_ID : 
			return addMethodCall(child, table, body, counter);
		case THIS_ID : 
			return addThisCall(child, table, body, counter);
		case SUPER_ID : 
			return addSuperCall(child, table, body, counter);
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
		if(!child.get(1).isTerminal() || (!child.get(1).getLexicalTerm().equals("id") && !WordIdentifier.isLexpr(child.get(1)))) {
			System.out.println("Expected id after a set");
			System.exit(9);
		}
		Clexpr v1;
		if(WordIdentifier.isLexpr(child.get(1))) {
			LexicalTerm t = child.get(1);
			if(t.getLexicalTerm().equals("this_int")) {
				String[] field = WordIdentifier.getLexpr(t);
				v1 = new CthisField(Integer.parseInt(field[1]));
			}
			else  {
				String[] field = WordIdentifier.getLexpr(t);
				v1 = new Cfield(field[0], Integer.parseInt(field[1]));
			}
		}
		else {
			v1 = new Cvar(child.get(1).getValue());
		}
		table.addLocal(child.get(1).getValue());
		Cass ass;
		if(child.get(2).isTerminal()) {
			if(WordIdentifier.isRexpr(child.get(2))) {
				Crexpr r = WordIdentifier.getRexpr(child.get(2)); 
				ass = new Cass(v1,r );
				body.add(ass);
			}
			else if(child.get(2).getLexicalTerm().equals("id")) {
				Cvar v2 = new Cvar(child.get(2).getValue());
				table.addLocal(child.get(2).getValue());
				
				ass = new Cass(v1,v2 );
				body.add(ass);
			}
			else {
				System.out.println("Expected number id or expr in ass");
				System.exit(15);
			}			
		}
		else {
			Cvar v2 = exploreCmd(child.get(2), table, body, counter);
			ass = new Cass(v1,v2);
			body.add(ass);
		}
		
		String name = counter.getAnonymousName();
		Cvar v = new Cvar(name);
		table.addLocal(name);
		Cass assi = new Cass(v,v1 );
		body.add(assi);
		return v;
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
			else if(child.get(1).getLexicalTerm().equals("id")) {
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
			else if(child.get(1).getLexicalTerm().equals("id")) {
				v1 = new Cvar(child.get(1).getValue());
				table.addLocal(child.get(1).getValue());
			}
			
			else {
				System.out.println("Expected number id or expr in return");
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
		for(int k = 1; k < child.size(); k++) {
			if(child.get(k).isTerminal()) {
				Cvar v1 = null;
				if(WordIdentifier.isRexpr(child.get(k))) {
					Crexpr r = WordIdentifier.getRexpr(child.get(k)); 
					String name = counter.getAnonymousName();
					table.addLocal(name);
					v1 = new Cvar(name);				
					body.add(new Cass(v1,r ));
					args[k - 1] = v1; 
					
					
				}
				else if(child.get(k).getLexicalTerm().equals("id")) {
					v1 = new Cvar(child.get(k).getValue());
					table.addLocal(child.get(k).getValue());
					args[k - 1] = v1; 
				}
				
				else {
					System.out.println(nom);
					System.out.println(child.get(k));
					System.out.println("Expected number id or expr simpleCall");
					System.exit(15);
				}	
				
				
				
			}
			else {
				
				Cvar v1 = exploreCmd(child.get(k), table, body, counter);
				args[k - 1] = v1; 
				
			}
		}
		String name = counter.getAnonymousName();
		table.addLocal(name);
		Cvar v2 = new Cvar(name);				
		body.add(new Cass(v2, new CsimpleCall(nom, args)));
		return v2;
	}
	
	
	private Cvar addMethodCall(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		String[] nom = child.get(0).getValue().split("[.]");
		Crexpr[] args = new Crexpr[child.size() - 1];
		for(int k = 1; k < child.size(); k++) {
			if(child.get(k).isTerminal()) {
				Cvar v1 = null;
				if(WordIdentifier.isRexpr(child.get(k))) {
					Crexpr r = WordIdentifier.getRexpr(child.get(k)); 
					String name = counter.getAnonymousName();
					table.addLocal(name);
					v1 = new Cvar(name);				
					body.add(new Cass(v1,r ));
					args[k - 1] = v1; 
					
					
				}
				else if(child.get(k).getLexicalTerm().equals("id")) {
					v1 = new Cvar(child.get(k).getValue());
					table.addLocal(child.get(k).getValue());
					args[k - 1] = v1; 
				}
				
				else {
					System.out.println("Expected number id or expr");
					System.exit(15);
				}	
				
				
				
			}
			else {
				Cvar v1 = exploreCmd(child.get(k), table, body, counter);
				args[k - 1] = v1; 
				
			}
		}
		String name = counter.getAnonymousName();
		table.addLocal(name);
		Cvar v2 = new Cvar(name);				
		body.add(new Cass(v2, new CvariableCall(nom[0], nom[1], args)));
		return v2;
	}
	
	
	private Cvar addThisCall(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		String[] nom = child.get(0).getValue().split("[.]");
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
				else if(child.get(i).getLexicalTerm().equals("id")) {
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
				Cvar v1 = exploreCmd(child.get(i), table, body, counter);
				args[i - 1] = v1; 
				
			}
		}
		String tempName = counter.getAnonymousName();
		Cvar temp = new Cvar(tempName);
		table.addLocal(tempName);
		body.add(new Cass(temp, new Cthis()));
		String name = counter.getAnonymousName();
		table.addLocal(name);
		Cvar v2 = new Cvar(name);				
		body.add(new Cass(v2, new CvariableCall(tempName, nom[1], args)));
		return v2;
	}
	
	private Cvar addSuperCall(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		String[] nom = child.get(0).getValue().split("[.]");
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
				else if(child.get(i).getLexicalTerm().equals("id")) {
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
				Cvar v1 = exploreCmd(child.get(i), table, body, counter);
				args[i - 1] = v1; 
				
			}
		}
		String name = counter.getAnonymousName();
		table.addLocal(name);
		Cvar v2 = new Cvar(name);				
		body.add(new Cass(v2, new CsuperCall(nom[1], args)));
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
				else if(child.get(i).getLexicalTerm().equals("id")) {
					Cvar v = new Cvar(child.get(i).getValue());
					table.addLocal(child.get(i).getValue());
					v1[i - 1] = v;
				}
				else {
					System.out.println("Expected number id or expr in op");
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
		
		body.add(new Cif(cond, b));
		
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
		body.add(new Cif(cond, b, c));
		
		
		
		//pour la compatibilité on renvoie rezo.
		Crexpr r = new Ci(0);
		String name = counter.getAnonymousName();
		Cvar v = new Cvar(name);
		table.addLocal(name);
		Cass ass = new Cass(v,r );
		body.add(ass);
		return v;
	}
	
	private Cvar addWhile(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		ArrayList<Cvar> eval = new ArrayList<Cvar>();
		Ccond cond = getCond(child.get(1), table, body, counter);
		Ccmd b = getInnerBody(child.get(2), table, counter);
		
		body.add(new Cwhile(cond, b));
		
		//pour la compatibilité on renvoie rezo.
		Crexpr r = new Ci(0);
		String name = counter.getAnonymousName();
		Cvar v = new Cvar(name);
		table.addLocal(name);
		Cass ass = new Cass(v,r );
		body.add(ass);
		return v;
	}
	
	private Cvar addNew(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(!child.get(1).isTerminal() || !child.get(1).getLexicalTerm().equals("number")) {
			System.out.println("Expected Number after new");
			System.exit(0);
		}
		
		int level = Integer.parseInt(child.get(1).getValue());
		if(level <= 0) {
			System.out.println("Level should be strict positive numbere at new");
			System.exit(0);
		}
			
		Cnew expr = new Cnew(level);
		
		
		
		//pour la compatibilité on renvoie rezo.
		
		String name = counter.getAnonymousName();
		Cvar v = new Cvar(name);
		table.addLocal(name);
		Cass ass = new Cass(v,expr );
		body.add(ass);
		return v;
	}
	
	private Cvar addNeg(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(1).isTerminal()) {
			Cvar v1 = null;
			if(WordIdentifier.isRexpr(child.get(1))) {
				Crexpr r = WordIdentifier.getRexpr(child.get(1)); 
				String name = counter.getAnonymousName();
				table.addLocal(name);
				v1 = new Cvar(name);				
				body.add(new Cass(v1,r ));
				
				
			}
			else if(child.get(1).getLexicalTerm().equals("id")) {
				v1 = new Cvar(child.get(1).getValue());
				table.addLocal(child.get(1).getValue());
			}
			
			else {
				System.out.println("Expected number id or expr");
				System.exit(15);
			}	
			
			String name = counter.getAnonymousName();
			Cvar v = new Cvar(name);
			table.addLocal(name);
			Cass ass = new Cass(v,new Cminus(v1));
			body.add(ass);
			
			return v;
		}
		else {
			Cvar v1 = exploreCmd(child.get(1), table, body, counter);
			body.add(new Creturn(v1));
			return v1;
		}
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
		
		
		t.getLexChildList().remove(0);
		t.getLexChildList().remove(t.getLexChildList().size() -1);
	}
	
	private int identifier(LexicalTerm t, int size) {
		String term = t.getLexicalTerm();
		
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
			if(size == 4) {
				return IF_ELSE;
				
			}
			if(size == 3) {
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
		if(term.equals("id_id")) {
			return ID_ID;
			
		}
		if(term.equals("this_id")) {
			return THIS_ID;
			
		}
		
		if(term.equals("super_id")) {
			return SUPER_ID;			
		}
		
		
		if(term.equals(LexicalTerm.UNARY_OP)) {
			
			if(t.getValue().equals("new")) {
				if(size != 2) {
					System.out.println("Expected int for a new");
					return ERROR;
				}
				return NEW;
			}
			if(t.getValue().equals("neg")) {
				if(size != 2) {
					System.out.println("Expected int for a neg");
					return ERROR;
				}
				return NEG;
			}
		}
		if(term.equals(LexicalTerm.BINARY_OP)) {
			if(WordIdentifier.isBinaryArithmetic(t)) {
				if(size != 3) {
					System.out.println("Expected expression and expression for an arithmetic operator");
					return ERROR;
				}
				return ARITHMETIC_OP;
			}
	
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
		if(op.equals("!")) {
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
			if(child.size() != 3) {
				System.out.println("Syntax error in condition OP");
				System.exit(17);
			}
			
			return addLogOp(child, table, body, counter);
		}
		System.out.println(op);
		return null;
	}
	
	private Ccond getNot(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(1).isTerminal()) {
			System.out.println("Syntax error at not, expected condition");
			System.exit(16);
		}
		return new Cnot(getCond(child.get(1), table, body, counter));
		
		
	}
	
	private Ccond getAnd(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(1).isTerminal() || child.get(2).isTerminal()) {
			System.out.println("Syntax error at AND, expected condition");
			System.exit(16);
		}
		return new Cand(getCond(child.get(1), table, body, counter), getCond(child.get(2), table, body, counter));
		
		
	}
	
	private Ccond getOr(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
		if(child.get(1).isTerminal() || child.get(2).isTerminal()) {
			System.out.println("Syntax error at AND, expected condition");
			System.exit(16);
		}
		return new Cor(getCond(child.get(1), table, body, counter),getCond(child.get(2), table, body, counter));
		
		
		
	}
	
	
	private Ccond addLogOp(List<LexicalTerm> child, MethodTable table, List<Ccmd> body, AnonymousCounter counter) {
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
				else if(child.get(i).getLexicalTerm().equals("id")) {
					Cvar v = new Cvar(child.get(i).getValue());
					table.addLocal(child.get(i).getValue());
					v1[i - 1] = v;
				}
				else {
					System.out.println("Expected number id or expr in conditionel operator");
					System.exit(15);
				}			
			}
			else {
				Cvar v = exploreCmd(child.get(i), table, body, counter);
				v1[i - 1] = v;
			}
		}

		
		return WordIdentifier.getBinaryLog(child.get(0), v1[0], v1[1]);
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
	public static final int NEW = 9;
	public static final int NEG = 10;
	public static final int ID_ID = 11;
	public static final int THIS_ID = 12;
	public static final int SUPER_ID = 13;
	
	
	
}
