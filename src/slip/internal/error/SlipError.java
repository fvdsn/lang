package slip.internal.error;

import java.util.Stack;

public class SlipError extends Exception {
	Stack<String> errorStack;
	String error;
	public SlipError(String message) {
		super(message);
		errorStack = new Stack<String>();
		error = message;
	}
	
	public void add(String s) {
		errorStack.push(s);
	}
	
	public void printStack() {
		System.out.println(error);
		for(String s : errorStack) {
			System.out.println("\t " + s);
		}
	}

}
