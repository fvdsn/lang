package slip.internal;

import slip.internal.error.SlipError;

public abstract class Cop extends AbstractNode
{
  public abstract boolean eval(Val v1, Val v2) throws SlipError;

}

