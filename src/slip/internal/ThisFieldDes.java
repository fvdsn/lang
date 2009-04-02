package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;


public class ThisFieldDes extends Des 
// left expression this.i
{
  public ThisFieldDes(int i){ super(0, i);}
  
  public void assign(Expr e, Env env, Store store) {
	  if(i > env.getLevel()) {
			System.out.println("Error : cannot acces to a field of this on level higher then the level of the method");
			System.exit(4);
		}
		
		
		Object o = store.get(env.get(x).val);
		Val v2 = o.get(i);
		Val newVal = e.getVal(env, store);
		//System.out.println("debug");
		//System.out.println(v2.type);
		//System.out.println(newVal.type);
		
		if(v2.type != Val.UNKNOW && v2.type != newVal.type) {
			System.out.println("Error : assignement on different type");
			System.exit(6);
		}
		else {
			o.set(i, newVal);

		}

	}

	@Override
	public Val getVal(Env e, Store st) {
		Val v = e.get(x);
	
		if(i > e.getLevel()) {
			System.out.println("Error : cannot acces to a field of this on level higher then the level of the method");
			System.exit(4);
		}
		if(v.val == 0) {
			System.out.println("Error : cannot acces to a field on a null reference");
			System.exit(4);
		}
		Object o = st.get(e.get(x).val);
		return o.get(i);
	}
}

