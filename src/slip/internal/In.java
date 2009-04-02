package slip.internal;

import java.util.Scanner;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class In extends Cmd // read(x)
{
 public static Scanner in = new Scanner(System.in);
  int x; 

  public In(int x){ this.x = x; }

  public String toString()
  { return "read(" + varName(x) + ")" ; }
  
  @Override
	public void execute(Env env, Store st) {
	  int i = in.nextInt();
	   env.set(x, new Val(Val.INTEGER, i));
	   
		
	}
                
}

