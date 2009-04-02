package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class Out extends Cmd // write(x)
{
  int x; 

  public Out(int x){ this.x = x; }

  public String toString()
  { return "write(" + varName(x) + ")" ; }
  
  @Override
	public void execute(Env env, Store store) {
		System.out.println(env.get(x));
		
	}
}

