package slip.internal;

import java.math.BigInteger;

import slip.internal.error.SlipError;

public class Remainder extends Aop // arithmetic operator %
{
  public Remainder()
  { super('%'); }
  
  @Override
	public BigInteger val(BigInteger v1, BigInteger v2) throws SlipError {
		if(v2.equals(BigInteger.ZERO)) {
			throw new SlipError("Error divide by 0");
			
		}
		return v1.remainder(v2);
	}
}

