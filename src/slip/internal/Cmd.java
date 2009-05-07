package slip.internal;

import slip.internal.error.SlipError;
import slip.internal.representation.Env;
import slip.internal.representation.Store;

public abstract class Cmd extends AbstractNode
{
  public abstract void execute(Env env, Store store) throws SlipError;
}

