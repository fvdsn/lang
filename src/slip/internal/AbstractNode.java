package slip.internal; 

public abstract class AbstractNode
{
   public final static String ELN = "\r" ;
   static String[] vt ;  // name of the local variables of the current method.
   static int magic = 0; // to avoid looping problems in toString().
 
   static String varName(int i)
   {
    if (i < 0) return "[bad variable number : " + i + "]";
    if (i == 0) return "result#" + i ;
    if (i <= vt.length) return vt[i - 1] + "#" + i ;
    return "#" + i ;
   }

}

