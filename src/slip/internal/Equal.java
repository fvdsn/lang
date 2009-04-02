package slip.internal;

public class  Equal extends Cop
{
  public Equal(){}

  public String toString(){ return "==" ; }
  
  @Override
	public boolean eval(Val v1, Val v2) {
		if(!(v1.type == v2.type)) {
			return false;
		}
		if(v1.type == Val.UNKNOW || v2.type == Val.UNKNOW) {
			return false;
		}
		if(v1.type == Val.ERROR || v2.type == Val.ERROR) {
			return false;
		}
		
		return v1.val == v2.val;
		
	}
}

