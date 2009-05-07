package slip.internal;

import slip.internal.error.SlipError;

public class  Less extends Cop
{
  
  public Less(){}

  public String toString()
  { return "<" ; }
  
  @Override
	public boolean eval(Val v1, Val v2) throws SlipError {
		if(v1.getType() != Val.INTEGER || v2.getType() != Val.INTEGER) {
			throw new SlipError("Error : Less illegal on pointer or unknow");
		}
		return v1.getIntval().compareTo(v2.getIntval()) < 0;
	}
}

