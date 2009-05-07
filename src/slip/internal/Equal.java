package slip.internal;

public class  Equal extends Cop
{
  public Equal(){}

  public String toString(){ return "==" ; }
  
  @Override
	public boolean eval(Val v1, Val v2) {
		if(!(v1.getType() == v2.getType())) {
			return false;
		}
		if(v1.getType() == Val.UNKNOW || v2.getType() == Val.UNKNOW) {
			return false;
		}
		if(v1.getType() == Val.ERROR || v2.getType() == Val.ERROR) {
			return false;
		}
		
		if(v1.getType() == Val.OBJECT) {
			return v1.getVal() == v2.getVal();
		}
		
		return v1.getIntval().equals(v2.getIntval());
		
	}
}

