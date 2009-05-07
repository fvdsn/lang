package slip.internal ;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class Cexpr extends Expr // arithmetic expression (expr1 aop expr2)
{
  Sexpr expr1; 
  Aop  aop;
  Sexpr expr2; 

  public Cexpr(Sexpr expr1, Aop aop, Sexpr expr2)
  { this.expr1 = expr1; this.aop = aop; this.expr2 = expr2; }

  public String toString()
  { return "" + expr1 + " " + aop
              + " " + expr2 ; }
  
  @Override
	public Val getVal(Env e, Store st) throws SlipError {
	  try {
	  	return aop.getVal(expr1.getVal(e, st), expr2.getVal(e, st));
	  }
	  catch(SlipError ex) {
		  ex.add("in Cexpr " + this.toString());
		  throw ex;
	  }
		
	}
}

