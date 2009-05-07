package slip.internal;

import java.math.BigInteger;

public class Plus extends Aop // arithmetic operator +
{
  public Plus()
  { super('+'); }
  
  
	public BigInteger val(BigInteger v1, BigInteger v2) {
		return v1.add(v2);
		
	}
}

