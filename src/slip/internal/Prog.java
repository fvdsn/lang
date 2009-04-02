package slip.internal; 

import slip.internal.representation.Env;
import slip.internal.representation.Store;




public class Prog extends AbstractNode // Program
{  
   Method[] meths; // Declaration of all methods
   
   
   
   public Prog(Method[] meths)
   { this.meths = meths ; 
   }

   public String toString()
   {
     String res = "" ;
     
     int i = 0 ;
     while (i!=meths.length)
     { res += meths[i] ; i++ ; }

     return res ;
   }
   
   
   public void execute() {
	   for(Method  method : this.meths) {
		   if(method.m.equals("main")) {
			   Store st = new Store();
			   Env env = new Env(-1);
			   method.execute(env, st);
		   }
	   }
   }
}

