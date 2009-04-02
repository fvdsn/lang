package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class Null extends Sexpr // null (the famous null reference) 
{
	public Null(){}

	public String toString()
	{ 
		return "null" ; 
	}

	@Override
	public Val getVal(Env e, Store st) {
		return new Val(Val.OBJECT, 0);
	}
}

