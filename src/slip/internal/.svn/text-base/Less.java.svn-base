package slip.internal;

public class  Less extends Cop
{
  
  public Less(){}

  public String toString()
  { return "<" ; }
  
  @Override
	public boolean eval(Val v1, Val v2) {
		if(v1.type != Val.INTEGER || v2.type != Val.INTEGER) {
			System.out.println("Error : Less illegal on pointer or unknow");
		}
		return v1.val < v2.val;
	}
}

