package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class VarDes extends Des // left expression x 
{
  public VarDes(int x){super(x, 0);}
  
  @Override
	public void assign(Expr e, Env env, Store store) {
	  Val v = env.get(x);
	  Val newVal =  e.getVal(env, store);

	  if(v == null || v.type == newVal.type || v.type == Val.UNKNOW || x == 0) { //existe pas encore ok
	  	  env.set(x, e.getVal(env, store));
	  }
	  else { //soucit on assigne
		  System.out.println(x);
		  System.out.println(varName(x));
		  System.out.println(e);
		  System.out.println(v);
		  System.out.println(env);
		  System.out.println("on se touche");
		 System.out.println("Error : try to change the type of the variable, forbidden");
		 System.exit(8);
	  }

		
	}
  
  
	public Val getVal(Env e, Store st) {
		return e.get(x);
	}
}

