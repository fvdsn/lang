package slip.internal;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

import slip.internal.error.SlipError;

public class Object implements Iterable<Val> {
	private int size;
	private Val[] val;
	
	public Object(int size) {
		this.size = size;
		val = new Val[size];
		for(int i = 0; i < size; i++) {
			val[i] = Val.unknowFactory();
		}
	}
	
	public Val get(int i) throws SlipError {
		if(i > size || i < 0) {
			throw new SlipError("ERROR : out of level access");
		}
		if(i == 0) {
			return new Val(new BigInteger(size + ""));
		}
		return val[i - 1];
	}
	
	public void set(int i, Val v) throws SlipError {
		if(i > size || i <= 0) {
			throw new SlipError("ERROR : out of level access");
		}
		val[i - 1] = v;
		
		
	}
	

	public Iterator<Val> iterator() {		
		return Arrays.asList(val).iterator();
	}
	
	public int getSize() {
		return size;
	}
}
