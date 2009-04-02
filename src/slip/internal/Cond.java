package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class Cond extends AbstractNode
{
  Sexpr expr1;
  Cop  cop;
  Sexpr expr2;

  public Cond(Sexpr expr1, Cop cop, Sexpr expr2)
  { this(cop) ; setExprs(expr1, expr2) ; }

  public Cond(Cop cop)
  { this.cop = cop ; }

  public void setExprs(Sexpr e1, Sexpr e2)
  { expr1 = e1 ; expr2 = e2 ; }

  public String toString()
  {
    return expr1 + " " + cop + " " + expr2 ;
  }
  
  //TODO 
  public boolean evalue(Env env, Store st) {
	  Val v1 = expr1.getVal(env, st);
	  Val v2 = expr2.getVal(env, st);
	  return cop.eval(v1, v2);
  }

}

