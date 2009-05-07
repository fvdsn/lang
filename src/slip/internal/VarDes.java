package slip.internal;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class VarDes extends Des // left expression x 
{
  public VarDes(int x){super(x, 0);}
  
  @Override
	public void assign(Expr e, Env env, Store store) throws SlipError {
	  try {
		  Val v = env.get(x);
		  Val newVal =  e.getVal(env, store);
	
		  if(v == null || v.getType() == newVal.getType() || v.getType() == Val.UNKNOW || x == 0) { //existe pas encore ok
		  	  env.set(x, e.getVal(env, store));
		  }
		  else { //soucit on assigne

			 throw new SlipError("Error : try to change the type of the variable, forbidden");
		  }
	  }
	  catch(SlipError ew) {
		  //ew.add("at VarDes " + x);
		  throw ew;
	  }		
	}
  
  
	public Val getVal(Env e, Store st) {
		return e.get(x);
	}
}

