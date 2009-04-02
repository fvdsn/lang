package slip.internal;

public abstract class Aop extends AbstractNode // arithmetic operator
{
  final char aop; // '+', '*', '-', '/', '%'

  Aop(char aop)
  { this.aop = aop; }

  public String toString(){ return "" + aop ; }
  
  public Val getVal(Val v1, Val v2) {
	  if(v1.type != Val.INTEGER || v2.type != Val.INTEGER) {
		  System.out.println("ERREUR : operation arithmetic on pointer forbidden");
		  System.exit(2);
		  return new Val(Val.ERROR, Val.ERROR_ARITHMETIC_ON_POINTER);
	  }
	  
	  return new Val(Val.INTEGER, val(v1.val, v2.val));
	  
  }
  
  public abstract int val(int v1, int v2);
}

