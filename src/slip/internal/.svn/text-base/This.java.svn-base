package slip.internal ;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class This extends Sexpr // this (as a right expression)
{
  public This(){}

  public String toString()
  { 
    return "this" ; 
  }
  
  @Override
	public Val getVal(Env e, Store st) {
		
		return e.get(0);
	}
}

