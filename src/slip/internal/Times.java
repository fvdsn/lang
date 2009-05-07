package slip.internal;

import java.math.BigInteger;

public class Times extends Aop // arithmetic operator *
{
  public Times()
  { super('*'); }
  
  @Override
	public BigInteger val(BigInteger v1, BigInteger v2) {
		return v1.multiply(v2);
	}
}

