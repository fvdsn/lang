package slip.internal;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;

public abstract class Expr extends AbstractNode // Any (right) expression
{
   public abstract Val getVal(Env e, Store st) throws SlipError;         
}

