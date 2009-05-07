package slip.internal;

import java.math.BigInteger;

import slip.internal.representation.Env;
import slip.internal.representation.Store;


public class I extends Sexpr // An integral value
{
  int i; // this integral value

  public I(int i){ this.i = i; }

  public String toString(){ return "" + i ; }
  
  @Override
	public Val getVal(Env e, Store st) {
		
	  return new Val(new BigInteger(new Integer(i).toString()));
	}

}

