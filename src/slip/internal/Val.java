package slip.internal;

import java.math.BigInteger;

public class Val {
	public static final int INTEGER = 1;
	public static final int OBJECT = 2;
	public static final int ERROR = 3;
	public static final int UNKNOW = 4;
	
	
	public static final int ERROR_OUT_OF_SIZE = 1;
	public static final int ERROR_ARITHMETIC_ON_POINTER = 1;
	
	private int type;
	private BigInteger intval;
	private int val;
	
	public Val(BigInteger val) {
		this.intval = val;
		this.type = INTEGER;
	}
	
	public Val(int val) {
		this.type = OBJECT;
		this.val = val;
	}
	
	private Val(int type, int val) {
		this.val = val;
		this.type = type;
	}
	
	public static Val unknowFactory() {
		return new Val(UNKNOW, 0);
	}
	
	@Override
	public String toString() {
		if(type == 1) {
			return ""+ intval;
		}
		if(type == 2) {
			return "ref:" + val;
		}
		if(type == 3) {
			return "Error";
		}
		else {
			return "undefined";
		}
			
		
	}
	
	
	public Val clone()  {
		Val v = new Val(this.type, this.val);
		v.intval = this.intval; //ok car immutable
		return v;
		
	}
	
	public int getType() {
		return type;
	}
	
	public int getVal() {
		return val;
	}
	
	public BigInteger getIntval() {
		return intval;
	}
	
	
}
