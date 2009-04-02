package slip.internal;


public abstract class Call extends Ass
{
  int x;    // result of the call
  int[] ap; // actual parameter list

  Call(int x, int[]ap){ this.x = x; this.ap = ap; }

  abstract String target();

  public String toString()
  {
    String res = varName(x) + " := "
           + target() + "(" ;
    if (ap.length!=0)
    {
       res += varName(ap[0]) ;

       int i = 1 ;
       while (i != ap.length)
       { 
         res += ", " + varName(ap[i]) ;
         i++ ;
       }
    }

    return res + ")";
  }
}

