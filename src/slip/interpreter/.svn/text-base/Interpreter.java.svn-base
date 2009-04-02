package slip.interpreter;

import slip.ConcreteToInternal;
import slip.internal.Prog;

public class Interpreter {
	public static Prog pro;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConcreteToInternal translator = new ConcreteToInternal(args[0]);
		
		
		try 
		{
		      pro = translator.translate();
		      //System.out.println(pro);
		      pro.execute();
		      
		}
		catch ( Exception e )
		{
			
		      System.err.println("Translation error : " + e.getMessage()) ;
		      e.printStackTrace();
		      System.exit(0) ;
		}

	}

}
