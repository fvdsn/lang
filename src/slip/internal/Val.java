package slip.internal;

public class Val {
	public static final int INTEGER = 1;
	public static final int OBJECT = 2;
	public static final int ERROR = 3;
	public static final int UNKNOW = 4;
	
	
	public static final int ERROR_OUT_OF_SIZE = 1;
	public static final int ERROR_ARITHMETIC_ON_POINTER = 1;
	int type;
	int val;
	
	public Val(int type, int val) {
		this.val = val;
		this.type = type;
	}
	
	public static Val unknowFactory() {
		return new Val(UNKNOW, 0);
	}
	
	@Override
	public String toString() {
		if(type == 1) {
			return ""+ val;
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
		return new Val(this.type, this.val);
		
	}
	
	public int getType() {
		return type;
	}
	
	public int getVal() {
		return val;
	}
	
	
}
