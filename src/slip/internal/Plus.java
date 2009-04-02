package slip.internal;

public class Plus extends Aop // arithmetic operator +
{
  public Plus()
  { super('+'); }
  
  
	public int val(int v1, int v2) {
		int re = v1 + v2;
		return re;
	}
}

