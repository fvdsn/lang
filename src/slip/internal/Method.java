package slip.internal; 

import slip.internal.representation.Env;
import slip.internal.representation.Store;

public class Method extends Stmt 
// Method declaration : name(x,...,x) ld {stmts} lf xr
{
   public String  m ; // Method identifier 
			
   boolean isStatic ; // true --> static ; false --> non static
			
   public int level ; // level >= 0 (if non static) : name ::= m/level
   public int arity ; // Number of formal parameters (x_1, ..., x_arity) (method arity)
   Stmt   l   ; // label of the first instruction (ld)
   int    nlv ; // Number of local variables (in source code)
   int    nav ; // Number of auxiliary variables (generated by the translation process)
   int    r   ; // result variable (xr) 0 <= r <= nlv + nav
   // final label (lf) : this
   String[]  var ; // the names of all local variables var[i] = i+1 th variable name

   public Method(String m, String[] var, boolean isStatic,
                 int level, int arity, 
                 int nlv, int r)
   {
     this.m = m ;
     this.var = var ;
     this.isStatic = isStatic ;
     this.level = level ;
     this.arity = arity ;
     this.nlv = nlv ;
     this.r = r ;  
   }

   public void setPar(Stmt l, int nav)
   {  this.l = l ; this.nav = nav ; }

   

   public String toString()
   {
      magic++ ;
      vt = var ;

      String res = toStringBis() + ELN + "{ " + (l.label) + ELN ;

      if (l != this) res += l ; // print all statements

      res += ELN + "} " + this.label + " " + varName(r) ;

      res += ELN + "end of method " + m + "." + ELN + ELN ;

      return res ;
   }

   public String toComment()
   {
      return "end of method " + m + " : " + this.label + " " + varName(r) ;
   }

   public String toStringBis()
   {
      

      String res = "method " + m ;

      if (level >= 0) res += "/" + level;
             res += "(";

      if (arity >= 1)
      {
         res += var[0];
         int i = 1;
         while (i!=arity)
         {
            res+= ", " + var[i] ; i++ ;
         }
      }

      res += ")" ;

      return res ;

   }
   

   public void execute(Env env, Store st) {
	   //System.out.println(l);
	  if(!(l instanceof Method)) {
		  l.execute(env, st);
	  }
   }

}

