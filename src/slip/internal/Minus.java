package slip.internal;

import java.math.BigInteger;

public class Minus extends Aop // arithmetic operator -
{
  public Minus()
  { super('-'); }
  
  @Override
	public BigInteger val(BigInteger v1, BigInteger v2) {
		return v1.subtract(v2);
	}
}

