package slip.internal.representation;

import java.util.HashMap;

import slip.internal.Object;

public class Store {
	private HashMap<Integer, Object> store;
	private int id = 1;
	
	public Store() {
		store = new HashMap<Integer, Object>();
	}
	
	//TODO unknown reference
	public Object get(int i) {
		return store.get(i);
	}
	
	public int newObject(int size) {
		id++;
		store.put(id, new Object(size));
		return id;
		
	}
}
