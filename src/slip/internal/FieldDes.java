package slip.internal;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class FieldDes extends Des 
// left expression x . i (i-th field of an object)
{
	public FieldDes(int x, int i) { 
		super(x, i) ; 
	}


	public void assign(Expr e, Env env, Store store) throws SlipError {
		try {
			Val v = env.get(x);
			if(v.getType() != Val.OBJECT) {
				throw new SlipError("Error : cannot acces to a field on non object variable");
			}
			if(v.getVal() == 0) {
				
			}
			Object o = store.get(env.get(x).getVal());
			Val v2 = o.get(i);
		
			Val newVal = e.getVal(env, store);
			if(v2.getType() != Val.UNKNOW && v2.getType() != newVal.getType()) {
				throw new SlipError("Error : assignement on different type");
			}
			else {
				o.set(i, newVal);
			}
		}
		catch(SlipError ex) {
			ex.add("at FieldDes " + x + "." + i);
			throw ex;
		}

		
		

	}

	@Override
	public Val getVal(Env e, Store st) throws SlipError {
		try {
			Val v = e.get(x);
			
			if(v.getType() != Val.OBJECT) {
				throw new SlipError("Error : cannot acces to a field on non object variable");
			}
			if(v.getVal() == 0) {
				throw new SlipError("Error : cannot acces to a field on a null reference");
			}
			Object o = st.get(e.get(x).getVal());
			return o.get(i);
		}
		catch(SlipError ex) {
			ex.add("at FieldDes " + x + "." + i);
			throw ex;
		}
	}
}

