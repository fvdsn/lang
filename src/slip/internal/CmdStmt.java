package slip.internal ;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;


public class CmdStmt extends Stmt // l cmd l
{
  // initial label == this
  Cmd  cmd;
  Stmt next; // final label

  public CmdStmt(Cmd cmd){ this.cmd = cmd ; }

  public CmdStmt(Cmd cmd, Stmt next)
  { this(cmd); setLabel(next) ; }


  public void setLabel(Stmt l){ next = l ; }

  public String toString()
  { 
    timeStamp = magic ; // to generate it only once.
    
    String res = "  " + toComment() ;

    if (next.timeStamp < magic & !(next instanceof Method)) 
       res += ELN + next ;

    return res ;
    
  }

  public String toComment()
  {    
    String res = "[ " + this.label + " : " + cmd 
                + " ; go to " + next.label + "]" ;

    return res ; 
  }
  
  public Stmt execute(Env env, Store st) throws SlipError {
	  try {
		  cmd.execute(env, st);
		  return next;
	  }
	  catch(SlipError e) {

		  e.add("at CmdStmt " + toComment());
		  throw e;
	  }
  }
}

