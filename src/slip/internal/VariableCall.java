package slip.internal ;

import java.util.HashMap;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;
import slip.interpreter.Interpreter;

public class VariableCall extends Call
{
  int target; // target of the call (a variable)
  String m;   // Method name

  public VariableCall(int x, int target, String m, int[] lfp)
  { super(x, lfp); this.target = target; this.m = m; }

  String target(){ return varName(target) + "." + m ; }
  
  @Override
	public void execute(Env env, Store store) throws SlipError {
		//x valeur de retour
	  	Val tar = env.get(target);
	  	//TODO vérifie que 
	  	if(tar.getType() != Val.OBJECT || tar.getVal() == 0) {
	  		throw new SlipError("Error : dynamic call on a non object variable or null");
	  	}
	  	Object ob = store.get(tar.getVal());
	  	
	  	
	  	HashMap<Integer, Method> list = new HashMap<Integer, Method>();
		for(Method m : Interpreter.pro.meths) {
			if(m.m.equals(this.m) && !m.isStatic && m.level <= ob.getSize()) {
				list.put(m.level, m);
			}
		}
		
		int lev = -1;
		for(int i = ob.getSize() ; i >= 0 && lev == -1; i--) {
			if(list.containsKey(i)) {
				lev = i;				
			}
		}
		if(lev == -1) {
			throw new SlipError("Unknow Procedure or function");
		}
		//System.out.println("level " + lev + " method " + list.get(lev).m );
		
		
		Env newEnv = new Env(lev, list.get(lev).getNumberVar() );
		//on place les arguments
		int i = 1;
		for(int j : this.ap) {
			newEnv.set(i, env.get(j).clone());
			i++;
		}
		//met le this
		
		newEnv.set(0, tar.clone());
		
		store.push(newEnv);
		list.get(lev).execute(newEnv, store);
		
		
		env.set(x, newEnv.get(0));
		store.pop();
		//System.out.println("EXIT pour pas avance");
		//System.exit(0);

	}
}

