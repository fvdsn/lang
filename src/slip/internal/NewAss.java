package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class NewAss extends Ass// x = new/i ;
{
  int x ; // where to put the reference to the object
  int i ; // level of the object to be created

  public NewAss(int x, int i){ this.x = x; this.i = i ;}

  public String toString()
  { 
    return varName(x) + " := new/" + i  ; 
  }
  
  @Override
	public void execute(Env env, Store store) {
		int ref = store.newObject(i);
		env.set(x, new Val(Val.OBJECT, ref));
		
	}
}

