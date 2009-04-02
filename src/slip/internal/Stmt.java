package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public abstract class Stmt extends AbstractNode
{
  static private int count = 1 ; // Counter of Stmt labels.
  public final String label ; // The label of this statement.
  int timeStamp = 0 ; // to avoid looping in toString() ;

  Stmt(){ label = "lab" + count ; count++ ;}
		
  abstract String toComment() ;
  
  abstract void execute(Env env, Store st);
}

