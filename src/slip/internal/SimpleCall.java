package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;
import slip.interpreter.Interpreter;

public class SimpleCall extends Call // x = m(x, ..., x);
{
	String m;   // Name of the method 

	public SimpleCall(int x,  String m, int[] lfp)
	{ super(x, lfp); this.m = m; }

	String target(){ return m ; }

	@Override
	public void execute(Env env, Store st) {
		Method m1 = null;
		for(Method m : Interpreter.pro.meths) {
			if(m.m.equals(this.m) && m.isStatic) {
				m1 = m;
			}
		}
		if(m1 == null) {
			System.out.println("Unknow Procedure or function");
			System.exit(1);
		}
		
		Env newEnv = new Env(-1, m1.getNumberVar());	
		int i = 1;
		for(int j : this.ap) {
			newEnv.set(i, env.get(j).clone());
			i++;
		}
		
		st.push(newEnv);
		m1.execute(newEnv, st);
		
		env.set(x, newEnv.get(0));
		st.pop();
	}
}

