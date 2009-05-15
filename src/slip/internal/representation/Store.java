package slip.internal.representation;


import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import slip.internal.Object;
import slip.internal.Val;

public class Store {
	private TreeMap<Integer, Object> store;
	private Stack<Env> refStack;
	private int id = 1;
	private GarbageCollector c;
	
	private long t = System.currentTimeMillis();
	
	public static final int INTERVAL_GC = 2000; 
	
	public Store() {
		refStack = new Stack<Env>();
		store = new TreeMap<Integer, Object>();
		//c = new GarbageCollector();
		//c.start();
	}
	
	public synchronized void end() {
		//c.end();
		//System.out.println(System.currentTimeMillis() - t);
	}
	
	//TODO unknown reference
	public synchronized Object get(int ref) {
		return store.get(ref);
	}
	
	public synchronized int newObject(int size) {
		id++;
		store.put(id, new Object(size));
		return id;
		
	}
	
	public String toString() {
		return "" + store.size();
	}
	
	public synchronized void destroy(int ref) {
		store.remove(store.get(ref));
		
	}
	
	public synchronized void garbageCollector() {
		TreeSet<Integer> set = new TreeSet<Integer>();
		for(Env e : refStack) {
			for(Val v : e ) { 
				if(v != null && v.getType() == Val.OBJECT) {
					
					if(set.add(v.getVal())) {
						Object o = this.get(v.getVal());
						exploreObject(set, o);
					}				
				}
			}
		}
		
		if(set.size() > 0 || set.size() != store.size()) {
			Integer[] find = set.toArray(new Integer[1]);
			Integer[] all = store.keySet().toArray(new Integer[1]);
		
			int j = 0;
			
			for(int i = 0; i < all.length; i++) {
				
				if(j <= find.length - 1 && find[j] != null && find[j].equals(all[i])) {
					j++;
				}
				else {
					
					if(all[i] != null)
						store.remove(all[i]);
				}
			}
		}		
	}
	
	private void exploreObject(Set<Integer> set, Object o) {
		if(o != null ) {
			for(Val v : o) {
				if(v != null && v.getType() == Val.OBJECT) {
					if(v != null && v.getType() == Val.OBJECT) {
						if(set.add(v.getVal())) {
							Object o1 = this.get(v.getVal());
							exploreObject(set, o1);
						}				
					}
				}
			}
		}
	}
	
	public synchronized void  push(Env env) {
		refStack.push(env);
	}
	
	public synchronized void pop() {
		refStack.pop();		
	}
	
	class GarbageCollector extends Thread {
		private boolean end = false;
		public void run() {
			while(!end) {
				try {
					Thread.sleep(INTERVAL_GC);
				}
				catch(InterruptedException e) {}
				garbageCollector();
			}
		}
		
		public void end() {
			end = true;
		}
	}
}
