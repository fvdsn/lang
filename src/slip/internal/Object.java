package slip.internal;

import java.util.Arrays;
import java.util.Iterator;

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
	
	public Val get(int i) {
		if(i > size || i < 0) {
			System.out.println("ERROR : out of level access");
			System.exit(5);
		}
		if(i == 0) {
			return new Val(Val.INTEGER, size);
		}
		return val[i - 1];
	}
	
	public void set(int i, Val v) {
		if(i > size || i <= 0) {
			System.out.println("ERROR : out of level access");
			System.exit(5);
		}
		val[i - 1] = v;
		
		
	}
	
	@Override
	public Iterator<Val> iterator() {		
		return Arrays.asList(val).iterator();
	}
	
	public int getSize() {
		return size;
	}
}
