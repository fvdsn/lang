package slip.internal;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;


public class ThisFieldDes extends Des 
// left expression this.i
{
  public ThisFieldDes(int i){ super(0, i);}
  
  public void assign(Expr e, Env env, Store store) throws SlipError {
	  try {
		  if(i > env.getLevel()) {
			  	throw new SlipError("Error : cannot acces to a field of this on level higher then the level of the method");
				
			}
		
		
		Object o = store.get(env.get(x).getVal());
		Val v2 = o.get(i);
		
			Val newVal = e.getVal(env, store);
			//System.out.println("debug");
			//System.out.println(v2.type);
			//System.out.println(newVal.type);
		
			if(v2.getType() != Val.UNKNOW && v2.getType() != newVal.getType()) {
				throw new SlipError("Error : assignement on different type");
			}
			else {
				o.set(i, newVal);

			}
		}
		catch(SlipError ex) {
			ex.add("at ThisFieldDes this." + i );
			throw ex;
		}

	}

	@Override
	public Val getVal(Env e, Store st) throws SlipError {
		try {
			Val v = e.get(x);
		
			if(i > e.getLevel()) {
				throw new SlipError("Error : cannot acces to a field of this on level higher then the level of the method");
				
			}
			if(v.getVal() == 0) {
				throw new SlipError("Error : cannot acces to a field on a null reference");				
			}
			Object o = st.get(e.get(x).getVal());
			return o.get(i);
		}
		catch(SlipError ew) {
			ew.add("at ThisFieldDes this." + i);
		}
		return Val.unknowFactory();
	}
}

