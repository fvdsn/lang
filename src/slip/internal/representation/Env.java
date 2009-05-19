package slip.internal.representation;

import java.util.Arrays;
import java.util.Iterator;

import slip.internal.Val;

public class Env implements Iterable<Val> {

	private Val[] env;
	private int level;
	
	public Env(int level, int size) {
		this.level = level;
		this.env = new Val[size + 1];
	}
	
	//TODO accès à une variable existe pas
	public Val get(int i) {
		return env[i];
	}
	
	public void set(int i, Val v) {
		env[i] = v;
	}
	
	public int getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < env.length; i++) {
			str += i + " => " + env[i] + "\n";
		}
		return str;
	}
	

	public Iterator<Val> iterator() {		
		return Arrays.asList(env).iterator();
	}
	
	
}
