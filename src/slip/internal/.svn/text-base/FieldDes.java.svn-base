package slip.internal;

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class FieldDes extends Des 
// left expression x . i (i-th field of an object)
{
	public FieldDes(int x, int i) { 
		super(x, i) ; 
	}


	public void assign(Expr e, Env env, Store store) {
		Val v = env.get(x);
		if(v.type != Val.OBJECT) {
			System.out.println("Error : cannot acces to a field on non object variable");
			System.exit(3);
		}
		if(v.val == 0) {
			System.out.println("Error : cannot acces to a field on a null reference");
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
		if(v.type != Val.OBJECT) {
			System.out.println("Error : cannot acces to a field on non object variable");
			System.exit(3);
		}
		if(v.val == 0) {
			System.out.println("Error : cannot acces to a field on a null reference");
			System.exit(4);
		}
		Object o = st.get(e.get(x).val);
		return o.get(i);
	}
}

