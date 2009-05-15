package happy.parser.syntax;

public class AnonymousCounter {
	private int counter = 0;
	public AnonymousCounter() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAnonymousName() {		
		return "A_" + counter++;
	}
}
