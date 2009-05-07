package slip.internal;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class NormalAss extends Ass
{
  Des left ; // left expression
  Expr right ; // right expression

  public NormalAss(Des left, Expr right)
  { this.left = left; this.right = right; }

  public void setExpr(Expr right)
  { this.right = right; }

  public String toString()
  { 
    return left + " := " + right  ; 
  }
  
  
  @Override
  public void execute(Env env, Store store) throws SlipError {
	  try {
		  left.assign(right, env, store);
	  }
	  catch(SlipError e) {
		  e.add("at Ass " + this.toString());
		  throw e;
	  }
  }
}

