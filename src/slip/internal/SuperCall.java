package slip.internal;

import java.util.HashMap;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;
import slip.interpreter.Interpreter;

public class SuperCall extends Call // x = super.m(x,..., x)
{
  String m; // Method name

  public SuperCall(int x,  String m, int[] lfp)
  { super(x, lfp); this.m = m; }

  String target(){ return "super." + m ; }

  
	public void execute(Env env, Store store) throws SlipError {
		
		try {
			//x valeur de retour
		  	Val tar = env.get(0);
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
			for(int i = env.getLevel() - 1  ; i >= 0 && lev == -1; i--) {
				if(list.containsKey(i)) {
					lev = i;				
				}
			}
			if(lev == -1) {
				throw new SlipError("Unknow Procedure or function");
			}
			//System.out.println("level " + lev + " method " + list.get(lev).m );
			
			
			Env newEnv = new Env(lev, list.get(lev).getNumberVar());
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
		}
		catch(SlipError e) {
			e.add("At Super Call " + m);
			throw e;
		}
		
		
		

		
	}
}

