package slip.internal;

import java.math.BigInteger;

import slip.internal.error.SlipError;

public abstract class Aop extends AbstractNode // arithmetic operator
{
  final char aop; // '+', '*', '-', '/', '%'

  Aop(char aop)
  { this.aop = aop; }

  public String toString(){ return "" + aop ; }
  
  public Val getVal(Val v1, Val v2) throws SlipError {
	  if(v1.getType() != Val.INTEGER || v2.getType() != Val.INTEGER) {		  
		  throw new SlipError("ERREUR : operation arithmetic on pointer forbidden");
	  }
	  
	  return new Val(val(v1.getIntval(), v2.getIntval()));
	  
  }
  
  public abstract BigInteger val(BigInteger v1, BigInteger v2) throws SlipError;
}

