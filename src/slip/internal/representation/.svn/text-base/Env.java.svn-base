package slip.internal.representation;

import java.util.HashMap;


import slip.internal.Val;

public class Env {

	private HashMap<Integer, Val> env;
	private int level;
	
	public Env(int level) {
		this.level = level;
		env = new HashMap<Integer, Val>();
	}
	
	//TODO accès à une variable existe pas
	public Val get(int i) {
		return env.get(i);
	}
	
	public void set(int i, Val v) {
		env.put(i, v);
	}
	
	public int getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i : env.keySet()) {
			str += i + " => " + env.get(i) + "\n";
		}
		return str;
	}
}
