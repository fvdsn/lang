package slip.internal;

public class Times extends Aop // arithmetic operator *
{
  public Times()
  { super('*'); }
  
  @Override
	public int val(int v1, int v2) {
		int re = v1 * v2;
		return re;
	}
}

