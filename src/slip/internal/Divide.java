package slip.internal;

public class Divide extends Aop // arithmetic operator /
{
  public Divide()
  { super('/'); }
  @Override
	public int val(int v1, int v2) {
		if(v2 == 0) {
			System.out.println("Error divide by 0");
			System.exit(3);
		}
		return v1 / v2;
	}
}

