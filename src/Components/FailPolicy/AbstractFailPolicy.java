package Components.FailPolicy;

import Components.Prefix;
import Components.Agent;

import java.util.Map;

public abstract class AbstractFailPolicy implements IFailPolicy {
    protected boolean didTheIterationFail;


    public AbstractFailPolicy()
    {

        didTheIterationFail = false;
    }

    @Override
    public boolean didTheIterationFail() {
        return didTheIterationFail;
    }

    public void setDidTheIterationFail(boolean didTheIterationFail) {
        this.didTheIterationFail = didTheIterationFail;
    }
}
